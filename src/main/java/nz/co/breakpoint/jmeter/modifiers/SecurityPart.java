package nz.co.breakpoint.jmeter.modifiers;

import org.apache.commons.lang3.StringUtils;
import org.apache.jmeter.testelement.AbstractTestElement;
import org.apache.wss4j.common.WSEncryptionPart;

/* This class models a part of the XML document that 
 * is to be secured (i.e. digitally signed or encrypted).
 * That part is identified by two attributes:
 * - Its local name (inherited property)
 * - Its namespace (URI)
 * A modifier attribute determines whether to sign/encrypt
 * the entire XML element or only its content.
 */
public class SecurityPart extends AbstractTestElement {
    
    private static final String NAMESPACE = "SecurityPart.Namespace", MODIFIER = "SecurityPart.Modifier", ID ="SecurityPart.ID";

    public SecurityPart() {}

    public String getNamespace() { return getPropertyAsString(NAMESPACE); }

    public void setNamespace(String namespace) { setProperty(NAMESPACE, namespace); }

    public String getModifier() { return getPropertyAsString(MODIFIER); }

    public void setId(String id){ setProperty(ID, id); }

    public String getId(){ return getPropertyAsString(ID); }

    /* @param modifier: "Content" or "Element"
     */
    public void setModifier(String modifier) { setProperty(MODIFIER, modifier); }

    /* Convenience method for Preprocessor accessor
     */
    public WSEncryptionPart getPart() {
        WSEncryptionPart part;
        if(!StringUtils.isEmpty(getId())){
            part = new WSEncryptionPart(getId(), getModifier());
        } else {
            part = new WSEncryptionPart(getName(), getNamespace(), getModifier());
        }
        part.setRequired(false); // treat as optional
        return part;
    }
}
