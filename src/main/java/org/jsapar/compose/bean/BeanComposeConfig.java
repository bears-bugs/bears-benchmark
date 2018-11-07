package org.jsapar.compose.bean;

import org.jsapar.error.ValidationAction;

/**
 * Configuration that controls behavior while composing beans.
 *
 * Created by stejon0 on 2016-10-16.
 */
@SuppressWarnings("WeakerAccess")
public class BeanComposeConfig {
    /**
     * The action to take if {@link BeanFactory} could not find a suitable bean class to create based on the
     * current line type.
     */
    private ValidationAction onUndefinedLineType = ValidationAction.EXCEPTION;

    /**
     * @return The action to take if {@link BeanFactory} could not find a suitable bean class to create based on the
     * current line type.
     */
    public ValidationAction getOnUndefinedLineType() {
        return onUndefinedLineType;
    }

    /**
     * @param onUndefinedLineType The action to take if {@link BeanFactory} could not find a suitable bean class to create based on the
     *                            current line type.
     */
    public void setOnUndefinedLineType(ValidationAction onUndefinedLineType) {
        this.onUndefinedLineType = onUndefinedLineType;
    }
}
