import { QuestionBase } from './question-base';

export class TextboxQuestion extends QuestionBase<string> {
    answerType = 'textbox';
    type: string;

    constructor(params: {} = {}) {
        super(params);
        this.type = params['answerType'] || '';
    }
}
