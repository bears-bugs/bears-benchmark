/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.fes20;

import org.eclipse.emf.ecore.EObject;

import org.eclipse.emf.ecore.util.FeatureMap;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Property Is Null Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.opengis.fes20.PropertyIsNullType#getExpressionGroup <em>Expression Group</em>}</li>
 *   <li>{@link net.opengis.fes20.PropertyIsNullType#getExpression <em>Expression</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.opengis.fes20.Fes20Package#getPropertyIsNullType()
 * @model extendedMetaData="name='PropertyIsNullType' kind='elementOnly'"
 * @generated
 */
public interface PropertyIsNullType extends ComparisonOpsType {
    /**
     * Returns the value of the '<em><b>Expression Group</b></em>' attribute list.
     * The list contents are of type {@link org.eclipse.emf.ecore.util.FeatureMap.Entry}.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Expression Group</em>' attribute list isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Expression Group</em>' attribute list.
     * @see net.opengis.fes20.Fes20Package#getPropertyIsNullType_ExpressionGroup()
     * @model dataType="org.eclipse.emf.ecore.EFeatureMapEntry" many="false"
     *        extendedMetaData="kind='group' name='expression:group' namespace='##targetNamespace'"
     * @generated
     */
    FeatureMap getExpressionGroup();

    /**
     * Returns the value of the '<em><b>Expression</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Expression</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Expression</em>' containment reference.
     * @see net.opengis.fes20.Fes20Package#getPropertyIsNullType_Expression()
     * @model containment="true" transient="true" changeable="false" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='expression' namespace='##targetNamespace' group='expression:group'"
     * @generated
     */
    EObject getExpression();

} // PropertyIsNullType
