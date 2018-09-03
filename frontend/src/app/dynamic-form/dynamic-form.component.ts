import {Component, Inject, Input, OnInit, Optional} from '@angular/core';
import {FormGroup} from '@angular/forms';

import {QuestionBase} from '../questions/question-base';
import {QuestionControlService} from '../questions/service/question-control.service';
import {Answer, Event, EventEntityService, ResourceUser} from '../';
import {MatDialog, MatDialogRef} from '@angular/material';
import {DIALOG_STYLE} from '../forms-common/dialog';
import {Config} from '../config/config';
import {Configuration} from '../configuration';
import {ConfigService} from '../config/config.service';
import {BASE_PATH} from '../variables';
import {ActivatedRoute} from '@angular/router';

@Component({
    selector: 'app-dynamic-form',
    templateUrl: './dynamic-form.component.html',
    providers: [QuestionControlService, EventEntityService]
})
export class DynamicFormComponent implements OnInit {

    @Input() questions: QuestionBase<any>[] = [];
    form: FormGroup;
    protected basePath = 'https://test.h2ms.org:81';
    configuration: Configuration;
    config: Config;
    loggedInUser: ResourceUser;

    constructor(private questionControlService: QuestionControlService,
                private eventEntityService: EventEntityService,
                public dialog: MatDialog,
                private actr: ActivatedRoute,
                @Optional()@Inject(BASE_PATH) basePath: string,
                @Optional() configuration: Configuration,
                @Optional() configService: ConfigService) {
        if (basePath) {
            this.basePath = basePath;
        }
        if (configuration) {
            this.configuration = configuration;
            this.basePath = basePath || configuration.basePath || this.basePath;
        }

        if (configService) {
            this.config = configService.getConfig();
            this.basePath = this.config.getBackendUrl();
        }
    }

    ngOnInit() {
        this.form = this.questionControlService.toFormGroup(this.questions);
        this.loggedInUser = this.actr.snapshot.data.userByEmailResolver._embedded.users[0];
    }

    onSubmit() {
        const answers: Array<Answer> = [];

        const values = this.form.value;

        // transform the value object into an array of Answers
        // TODO: unanswered, not required questions shouldn't be sent
        for (const property of Object.entries(values)) {
            if (property[0] !== 'location' && property[0] !== 'subject') {
                const answer: Answer = {
                    question: property[0],
                    // TODO: this is a workaround for untouched booleans, needs to be cleaned up at some point
                    value: property[1] === undefined ? false : property[1]
                };
                answers.push(answer);
            }
        }

        const event: Event = {
            eventTemplate: this.basePath.concat('/eventTemplates/1'),
            answers: answers,
            location: this.form.value['location'],
            observer: this.loggedInUser._links.self.href,
            subject: this.form.value['subject'],
            timestamp: new Date()
        };

        this.eventEntityService.saveEventUsingPOST(event)
            .subscribe(() => this.openDialog());

    }

    openDialog(): void {
        this.dialog.open(FormSubmissionDialogComponent, DIALOG_STYLE);
    }
}

@Component({
    selector: 'app-form-submission-dialog',
    templateUrl: 'form-submission-dialog.html',
})
export class FormSubmissionDialogComponent {

    constructor(public dialogRef: MatDialogRef<FormSubmissionDialogComponent>) {
    }

    closeDialog(): void {
        this.dialogRef.close();
    }
}


