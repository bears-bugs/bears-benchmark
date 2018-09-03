import { QuestionBase } from './question-base';

export class CheckboxQuestion extends QuestionBase<string> {
    answerType = 'checkbox';

    constructor(params: {} = {}) {
        super(params);
    }
}
