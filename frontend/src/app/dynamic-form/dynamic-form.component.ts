import {Component, Input, OnInit} from '@angular/core';
import {FormGroup} from '@angular/forms';

import {QuestionBase} from '../questions/question-base';
import {QuestionControlService} from '../questions/service/question-control.service';
import {Answer, Event, EventEntityService} from '../';
import {MatDialog, MatDialogRef} from '@angular/material';

@Component({
    selector: 'app-dynamic-form',
    templateUrl: './dynamic-form.component.html',
    providers: [QuestionControlService, EventEntityService]
})
export class DynamicFormComponent implements OnInit {

    @Input() questions: QuestionBase<any>[] = [];
    form: FormGroup;

    constructor(private questionControlService: QuestionControlService,
                private eventEntityService: EventEntityService,
                public dialog: MatDialog) {
    }

    ngOnInit() {
        this.form = this.questionControlService.toFormGroup(this.questions);
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
            eventTemplate: 'http://test.h2ms.org:81/eventTemplates/1',
            answers: answers,
            location: this.form.value['location'],
            observer: this.form.value['subject'],
            subject: this.form.value['subject'],
            timestamp: new Date()
        };

        this.eventEntityService.saveEventUsingPOST(event).subscribe(() => this.openDialog());

    }

    openDialog(): void {
        const dialogRef = this.dialog.open(FormSubmissionDialogComponent, {
            width: '270px',
        });
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


