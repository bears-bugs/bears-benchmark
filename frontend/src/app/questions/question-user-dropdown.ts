import { QuestionBase } from './question-base';

export class UserDropdownQuestion extends QuestionBase<string> {
    answerType = 'user_dropdown';
    options: object[] = [];

    constructor(params: {} = {}) {
        super(params);
        this.options = params['options'] || [];
    }
}
