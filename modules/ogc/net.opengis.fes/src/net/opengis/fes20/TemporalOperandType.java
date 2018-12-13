/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.fes20;

import javax.xml.namespace.QName;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Temporal Operand Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.opengis.fes20.TemporalOperandType#getName <em>Name</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.opengis.fes20.Fes20Package#getTemporalOperandType()
 * @model extendedMetaData="name='TemporalOperand_._type' kind='empty'"
 * @generated
 */
public interface TemporalOperandType extends EObject {
    /**
     * Returns the value of the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Name</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Name</em>' attribute.
     * @see #setName(QName)
     * @see net.opengis.fes20.Fes20Package#getTemporalOperandType_Name()
     * @model dataType="org.eclipse.emf.ecore.xml.type.QName" required="true"
     *        extendedMetaData="kind='attribute' name='name'"
     * @generated
     */
    QName getName();

    /**
     * Sets the value of the '{@link net.opengis.fes20.TemporalOperandType#getName <em>Name</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Name</em>' attribute.
     * @see #getName()
     * @generated
     */
    void setName(QName value);

} // TemporalOperandType
