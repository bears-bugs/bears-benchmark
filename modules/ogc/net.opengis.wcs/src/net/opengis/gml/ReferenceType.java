/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.gml;

import org.eclipse.emf.ecore.EObject;

import org.w3.xlink.ActuateType;
import org.w3.xlink.ShowType;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Reference Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 *  A pattern or base for derived types used to specify complex types corresponding to a UML aggregation association.  An instance of this type serves as a pointer to a remote Object.
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.opengis.gml.ReferenceType#getActuate <em>Actuate</em>}</li>
 *   <li>{@link net.opengis.gml.ReferenceType#getArcrole <em>Arcrole</em>}</li>
 *   <li>{@link net.opengis.gml.ReferenceType#getHref <em>Href</em>}</li>
 *   <li>{@link net.opengis.gml.ReferenceType#getRemoteSchema <em>Remote Schema</em>}</li>
 *   <li>{@link net.opengis.gml.ReferenceType#getRole <em>Role</em>}</li>
 *   <li>{@link net.opengis.gml.ReferenceType#getShow <em>Show</em>}</li>
 *   <li>{@link net.opengis.gml.ReferenceType#getTitle <em>Title</em>}</li>
 *   <li>{@link net.opengis.gml.ReferenceType#getType <em>Type</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.opengis.gml.GmlPackage#getReferenceType()
 * @model extendedMetaData="name='ReferenceType' kind='empty'"
 * @generated
 */
public interface ReferenceType extends EObject {
    /**
	 * Returns the value of the '<em><b>Actuate</b></em>' attribute.
	 * The default value is <code>"onLoad"</code>.
	 * The literals are from the enumeration {@link org.w3.xlink.ActuateType}.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * 
	 *         The 'actuate' attribute is used to communicate the desired timing
	 *         of traversal from the starting resource to the ending resource;
	 *         it's value should be treated as follows:
	 *         onLoad - traverse to the ending resource immediately on loading
	 *                  the starting resource
	 *         onRequest - traverse from the starting resource to the ending
	 *                     resource only on a post-loading event triggered for
	 *                     this purpose
	 *         other - behavior is unconstrained; examine other markup in link
	 *                 for hints
	 *         none - behavior is unconstrained
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Actuate</em>' attribute.
	 * @see org.w3.xlink.ActuateType
	 * @see #isSetActuate()
	 * @see #unsetActuate()
	 * @see #setActuate(ActuateType)
	 * @see net.opengis.gml.GmlPackage#getReferenceType_Actuate()
	 * @model default="onLoad" unsettable="true"
	 *        extendedMetaData="kind='attribute' name='actuate' namespace='http://www.w3.org/1999/xlink'"
	 * @generated
	 */
    ActuateType getActuate();

    /**
	 * Sets the value of the '{@link net.opengis.gml.ReferenceType#getActuate <em>Actuate</em>}' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Actuate</em>' attribute.
	 * @see org.w3.xlink.ActuateType
	 * @see #isSetActuate()
	 * @see #unsetActuate()
	 * @see #getActuate()
	 * @generated
	 */
    void setActuate(ActuateType value);

    /**
	 * Unsets the value of the '{@link net.opengis.gml.ReferenceType#getActuate <em>Actuate</em>}' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @see #isSetActuate()
	 * @see #getActuate()
	 * @see #setActuate(ActuateType)
	 * @generated
	 */
    void unsetActuate();

    /**
	 * Returns whether the value of the '{@link net.opengis.gml.ReferenceType#getActuate <em>Actuate</em>}' attribute is set.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return whether the value of the '<em>Actuate</em>' attribute is set.
	 * @see #unsetActuate()
	 * @see #getActuate()
	 * @see #setActuate(ActuateType)
	 * @generated
	 */
    boolean isSetActuate();

    /**
	 * Returns the value of the '<em><b>Arcrole</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Arcrole</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
	 * @return the value of the '<em>Arcrole</em>' attribute.
	 * @see #setArcrole(String)
	 * @see net.opengis.gml.GmlPackage#getReferenceType_Arcrole()
	 * @model dataType="org.eclipse.emf.ecore.xml.type.AnyURI"
	 *        extendedMetaData="kind='attribute' name='arcrole' namespace='http://www.w3.org/1999/xlink'"
	 * @generated
	 */
    String getArcrole();

    /**
	 * Sets the value of the '{@link net.opengis.gml.ReferenceType#getArcrole <em>Arcrole</em>}' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Arcrole</em>' attribute.
	 * @see #getArcrole()
	 * @generated
	 */
    void setArcrole(String value);

    /**
	 * Returns the value of the '<em><b>Href</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Href</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
	 * @return the value of the '<em>Href</em>' attribute.
	 * @see #setHref(String)
	 * @see net.opengis.gml.GmlPackage#getReferenceType_Href()
	 * @model dataType="org.eclipse.emf.ecore.xml.type.AnyURI"
	 *        extendedMetaData="kind='attribute' name='href' namespace='http://www.w3.org/1999/xlink'"
	 * @generated
	 */
    String getHref();

    /**
	 * Sets the value of the '{@link net.opengis.gml.ReferenceType#getHref <em>Href</em>}' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Href</em>' attribute.
	 * @see #getHref()
	 * @generated
	 */
    void setHref(String value);

    /**
	 * Returns the value of the '<em><b>Remote Schema</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Reference to an XML Schema fragment that specifies the content model of the property's value. This is in conformance with the XML Schema Section 4.14 Referencing Schemas from Elsewhere.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Remote Schema</em>' attribute.
	 * @see #setRemoteSchema(String)
	 * @see net.opengis.gml.GmlPackage#getReferenceType_RemoteSchema()
	 * @model dataType="org.eclipse.emf.ecore.xml.type.AnyURI"
	 *        extendedMetaData="kind='attribute' name='remoteSchema' namespace='##targetNamespace'"
	 * @generated
	 */
    String getRemoteSchema();

    /**
	 * Sets the value of the '{@link net.opengis.gml.ReferenceType#getRemoteSchema <em>Remote Schema</em>}' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Remote Schema</em>' attribute.
	 * @see #getRemoteSchema()
	 * @generated
	 */
    void setRemoteSchema(String value);

    /**
	 * Returns the value of the '<em><b>Role</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Role</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
	 * @return the value of the '<em>Role</em>' attribute.
	 * @see #setRole(String)
	 * @see net.opengis.gml.GmlPackage#getReferenceType_Role()
	 * @model dataType="org.eclipse.emf.ecore.xml.type.AnyURI"
	 *        extendedMetaData="kind='attribute' name='role' namespace='http://www.w3.org/1999/xlink'"
	 * @generated
	 */
    String getRole();

    /**
	 * Sets the value of the '{@link net.opengis.gml.ReferenceType#getRole <em>Role</em>}' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Role</em>' attribute.
	 * @see #getRole()
	 * @generated
	 */
    void setRole(String value);

    /**
	 * Returns the value of the '<em><b>Show</b></em>' attribute.
	 * The default value is <code>"new"</code>.
	 * The literals are from the enumeration {@link org.w3.xlink.ShowType}.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * 
	 *         The 'show' attribute is used to communicate the desired presentation
	 *         of the ending resource on traversal from the starting resource; it's
	 *         value should be treated as follows:
	 *         new - load ending resource in a new window, frame, pane, or other
	 *               presentation context
	 *         replace - load the resource in the same window, frame, pane, or
	 *                   other presentation context
	 *         embed - load ending resource in place of the presentation of the
	 *                 starting resource
	 *         other - behavior is unconstrained; examine other markup in the
	 *                 link for hints
	 *         none - behavior is unconstrained
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Show</em>' attribute.
	 * @see org.w3.xlink.ShowType
	 * @see #isSetShow()
	 * @see #unsetShow()
	 * @see #setShow(ShowType)
	 * @see net.opengis.gml.GmlPackage#getReferenceType_Show()
	 * @model default="new" unsettable="true"
	 *        extendedMetaData="kind='attribute' name='show' namespace='http://www.w3.org/1999/xlink'"
	 * @generated
	 */
    ShowType getShow();

    /**
	 * Sets the value of the '{@link net.opengis.gml.ReferenceType#getShow <em>Show</em>}' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Show</em>' attribute.
	 * @see org.w3.xlink.ShowType
	 * @see #isSetShow()
	 * @see #unsetShow()
	 * @see #getShow()
	 * @generated
	 */
    void setShow(ShowType value);

    /**
	 * Unsets the value of the '{@link net.opengis.gml.ReferenceType#getShow <em>Show</em>}' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @see #isSetShow()
	 * @see #getShow()
	 * @see #setShow(ShowType)
	 * @generated
	 */
    void unsetShow();

    /**
	 * Returns whether the value of the '{@link net.opengis.gml.ReferenceType#getShow <em>Show</em>}' attribute is set.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return whether the value of the '<em>Show</em>' attribute is set.
	 * @see #unsetShow()
	 * @see #getShow()
	 * @see #setShow(ShowType)
	 * @generated
	 */
    boolean isSetShow();

    /**
	 * Returns the value of the '<em><b>Title</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Title</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
	 * @return the value of the '<em>Title</em>' attribute.
	 * @see #setTitle(String)
	 * @see net.opengis.gml.GmlPackage#getReferenceType_Title()
	 * @model dataType="org.eclipse.emf.ecore.xml.type.String"
	 *        extendedMetaData="kind='attribute' name='title' namespace='http://www.w3.org/1999/xlink'"
	 * @generated
	 */
    String getTitle();

    /**
	 * Sets the value of the '{@link net.opengis.gml.ReferenceType#getTitle <em>Title</em>}' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Title</em>' attribute.
	 * @see #getTitle()
	 * @generated
	 */
    void setTitle(String value);

    /**
	 * Returns the value of the '<em><b>Type</b></em>' attribute.
	 * The default value is <code>"simple"</code>.
	 * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Type</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
	 * @return the value of the '<em>Type</em>' attribute.
	 * @see #isSetType()
	 * @see #unsetType()
	 * @see #setType(String)
	 * @see net.opengis.gml.GmlPackage#getReferenceType_Type()
	 * @model default="simple" unsettable="true" dataType="org.eclipse.emf.ecore.xml.type.String"
	 *        extendedMetaData="kind='attribute' name='type' namespace='http://www.w3.org/1999/xlink'"
	 * @generated
	 */
    String getType();

    /**
	 * Sets the value of the '{@link net.opengis.gml.ReferenceType#getType <em>Type</em>}' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Type</em>' attribute.
	 * @see #isSetType()
	 * @see #unsetType()
	 * @see #getType()
	 * @generated
	 */
    void setType(String value);

    /**
	 * Unsets the value of the '{@link net.opengis.gml.ReferenceType#getType <em>Type</em>}' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @see #isSetType()
	 * @see #getType()
	 * @see #setType(String)
	 * @generated
	 */
    void unsetType();

    /**
	 * Returns whether the value of the '{@link net.opengis.gml.ReferenceType#getType <em>Type</em>}' attribute is set.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return whether the value of the '<em>Type</em>' attribute is set.
	 * @see #unsetType()
	 * @see #getType()
	 * @see #setType(String)
	 * @generated
	 */
    boolean isSetType();

} // ReferenceType
