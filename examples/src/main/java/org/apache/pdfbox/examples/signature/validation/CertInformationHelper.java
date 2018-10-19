/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.pdfbox.examples.signature.validation;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PublicKey;
import java.security.SignatureException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Enumeration;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.pdfbox.examples.signature.validation.CertInformationCollector.CertSignatureInformation;
import org.apache.pdfbox.util.Hex;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.DEROctetString;
import org.bouncycastle.asn1.DERTaggedObject;
import org.bouncycastle.asn1.DLSequence;
import org.bouncycastle.asn1.x509.GeneralName;
import org.bouncycastle.asn1.x509.X509ObjectIdentifiers;
import org.bouncycastle.cert.jcajce.JcaX509ExtensionUtils;

public class CertInformationHelper
{
    private static final Log LOG = LogFactory.getLog(CertInformationHelper.class);

    private CertInformationHelper()
    {
    }

    /**
     * Gets the SHA-1-Hash has of given byte[]-content.
     * 
     * @param content to be hashed
     * @return SHA-1 hash String
     */
    protected static String getSha1Hash(byte[] content)
    {
        try
        {
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            return Hex.getString(md.digest(content));
        }
        catch (NoSuchAlgorithmException e)
        {
            LOG.error("No SHA-1 Algorithm found", e);
        }
        return null;
    }

    /**
     * Checks whether the given certificate is self-signed (root).
     * 
     * @param cert to be checked
     * @return true when it is a self-signed certificate
     * @throws CertificateProccessingException containing the cause, on multiple exception with the given data
     */
    public static boolean isSelfSigned(X509Certificate cert) throws CertificateProccessingException
    {
        return verify(cert, cert.getPublicKey());
    }

    /**
     * Verifies whether the certificate is signed by the given public key. Can be done to check
     * signature chain. Idea and code are from Julius Musseau at:
     * https://stackoverflow.com/a/10822177/2497581
     *
     * @param cert Child certificate to check
     * @param key Fathers public key to check
     * @return true when the certificate is signed by the public key
     * @throws CertificateProccessingException containing the cause, on multiple exception with the
     * given data
     */
    public static boolean verify(X509Certificate cert, PublicKey key)
            throws CertificateProccessingException
    {
        try
        {
            String sigAlg = cert.getSigAlgName();
            String keyAlg = key.getAlgorithm();
            sigAlg = sigAlg != null ? sigAlg.trim().toUpperCase() : "";
            keyAlg = keyAlg != null ? keyAlg.trim().toUpperCase() : "";
            if (keyAlg.length() >= 2 && sigAlg.endsWith(keyAlg))
            {
                try
                {
                    cert.verify(key);
                    return true;
                }
                catch (SignatureException se)
                {
                    LOG.debug("Couldn't get signature information - returning false", se);
                    return false;
                }
            }
            else
            {
                return false;
            }
        }
        catch (InvalidKeyException | CertificateException | NoSuchAlgorithmException
                | NoSuchProviderException e)
        {
            throw new CertificateProccessingException(e);
        }
    }

    /**
     * Extracts authority information access extension values from the given data. The Data
     * structure has to be implemented as described in RFC 2459, 4.2.2.1.
     *
     * @param extensionValue byte[] of the extension value.
     * @param certInfo where to put the found values
     * @throws IOException when there is a problem with the extensionValue
     */
    protected static void getAuthorityInfoExtensionValue(byte[] extensionValue,
            CertSignatureInformation certInfo) throws IOException
    {
        ASN1Sequence asn1Seq = (ASN1Sequence) JcaX509ExtensionUtils.parseExtensionValue(extensionValue);
        Enumeration<?> objects = asn1Seq.getObjects();
        while (objects.hasMoreElements())
        {
            // AccessDescription
            ASN1Sequence obj = (ASN1Sequence) objects.nextElement();
            ASN1ObjectIdentifier oid = (ASN1ObjectIdentifier) obj.getObjectAt(0);
            // accessLocation
            DERTaggedObject location = (DERTaggedObject) obj.getObjectAt(1);

            if (oid.equals(X509ObjectIdentifiers.id_ad_ocsp)
                    && location.getTagNo() == GeneralName.uniformResourceIdentifier)
            {
                DEROctetString url = (DEROctetString) location.getObject();
                certInfo.setOcspUrl(new String(url.getOctets()));
            }
            else if (oid.equals(X509ObjectIdentifiers.id_ad_caIssuers))
            {
                DEROctetString uri = (DEROctetString) location.getObject();
                certInfo.setIssuerUrl(new String(uri.getOctets()));
            }
        }
    }

    /**
     * Gets the first CRL URL from given extension value. Structure has to be
     * built as in 4.2.1.14 CRL Distribution Points of RFC 2459.
     *
     * @param extensionValue to get the extension value from
     * @return first CRL- URL or null
     * @throws IOException when there is a problem with the extensionValue
     */
    protected static String getCrlUrlFromExtensionValue(byte[] extensionValue) throws IOException
    {
        ASN1Sequence asn1Seq = (ASN1Sequence) JcaX509ExtensionUtils.parseExtensionValue(extensionValue);
        Enumeration<?> objects = asn1Seq.getObjects();

        while (objects.hasMoreElements())
        {
            DLSequence obj = (DLSequence) objects.nextElement();

            DERTaggedObject derTagged = (DERTaggedObject) obj.getObjectAt(0);
            derTagged = (DERTaggedObject) derTagged.getObject();
            derTagged = (DERTaggedObject) derTagged.getObject();
            DEROctetString uri = (DEROctetString) derTagged.getObject();
            String url = new String(uri.getOctets());
            // TODO Check for: DistributionPoint ::= SEQUENCE (see RFC 2459), multiples can be possible.

            // return first http(s)-Url for crl
            if (url.startsWith("http"))
            {
                return url;
            }
        }
        return null;
    }
}
