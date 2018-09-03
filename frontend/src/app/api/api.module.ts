import { NgModule, ModuleWithProviders, SkipSelf, Optional } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HttpClientModule } from '@angular/common/http';
import { Configuration } from '../configuration';

import { BasicErrorControllerService } from './basicErrorController.service';
import { EventControllerService } from './eventController.service';
import { EventEntityService } from './eventEntity.service';
import { EventTemplateEntityService } from './eventTemplateEntity.service';
import { LocationEntityService } from './locationEntity.service';
import { ProfileControllerService } from './profileController.service';
import { QuestionEntityService } from './questionEntity.service';
import { ReaderEntityService } from './readerEntity.service';
import { RoleEntityService } from './roleEntity.service';
import { UserEntityService } from './userEntity.service';
import { WristBandEntityService } from './wristBandEntity.service';

@NgModule({
  imports:      [ CommonModule, HttpClientModule ],
  declarations: [],
  exports:      [],
  providers: [
    BasicErrorControllerService,
    EventControllerService,
    EventEntityService,
    EventTemplateEntityService,
    LocationEntityService,
    ProfileControllerService,
    QuestionEntityService,
    ReaderEntityService,
    RoleEntityService,
    UserEntityService,
    WristBandEntityService ]
})
export class ApiModule {
    public static forRoot(configurationFactory: () => Configuration): ModuleWithProviders {
        return {
            ngModule: ApiModule,
            providers: [ { provide: Configuration, useFactory: configurationFactory } ]
        }
    }

    constructor( @Optional() @SkipSelf() parentModule: ApiModule) {
        if (parentModule) {
            throw new Error('ApiModule is already loaded. Import your base AppModule only.');
        }
    }
}
