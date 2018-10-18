package org.hisp.dhis.user.action;

/*
 * Copyright (c) 2004-2018, University of Oslo
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * Redistributions of source code must retain the above copyright notice, this
 * list of conditions and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 * Neither the name of the HISP project nor the names of its contributors may
 * be used to endorse or promote products derived from this software without
 * specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 * ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

import org.hisp.dhis.common.IdentifiableObjectUtils;
import org.hisp.dhis.user.CurrentUserService;
import org.hisp.dhis.user.User;
import org.hisp.dhis.user.UserCredentials;
import org.hisp.dhis.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;

import com.opensymphony.xwork2.Action;

/**
 * @author Lars Helge Overland
 */
public class DisableUserAction
    implements Action
{
    @Autowired
    private UserService userService;

    @Autowired
    private CurrentUserService currentUserService;
    
    private String username;
    
    public void setUsername( String username )
    {
        this.username = username;
    }
    
    private boolean enable = false;

    public void setEnable( boolean enable )
    {
        this.enable = enable;
    }

    @Override
    public String execute()
    {
        UserCredentials credentials = userService.getUserCredentialsByUsername( username );
        
        if ( credentials == null )
        {
            return ERROR;
        }
        
        User currentUser = currentUserService.getCurrentUser();
        
        if ( currentUser == null || currentUser.getUserCredentials() == null )
        {
            return ERROR;
        }

        if ( !userService.canAddOrUpdateUser( IdentifiableObjectUtils.getUids( credentials.getUserInfo().getGroups() ) )
            || !currentUser.getUserCredentials().canModifyUser( credentials ) )
        {
            return ERROR;
        }
        
        credentials.setDisabled( !enable );
        
        userService.updateUserCredentials( credentials );
        
        return SUCCESS;
    }    
}
