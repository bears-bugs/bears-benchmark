import { Component, Input } from '@angular/core';
import { FormGroup } from '@angular/forms';

import { QuestionBase } from '../questions/question-base';

@Component({
    selector: 'app-question',
    templateUrl: './dynamic-form-question.component.html',
    styleUrls: ['../card.css', './dynamic-form-question.component.css']
})
export class DynamicFormQuestionComponent {
    @Input() question: QuestionBase<any>;
    @Input() form: FormGroup;

    isValid(): boolean {
        if (this !== undefined && Object.keys(this.form.controls).length !== 0) {
            return this.form.controls[this.question.id].valid;
        }
        return false;
    }

    isRequired(): boolean {
        return !this.isValid();
    }
}
