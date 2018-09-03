export class QuestionBase<T> {
    value: T;
    id: number;
    question: string;
    required: boolean;
    priority: number;
    answerType: string;
    options: Array<object>;

    constructor(params: {
        value?: T,
        id?: number,
        question?: string,
        required?: boolean,
        priority?: number,
        answerType?: string,
        options?: Array<object>
    } = {}) {
        this.value = params.value;
        this.id = params.id === undefined ? 0 : params.id;
        this.question = params.question || '';
        this.required = !!params.required;
        this.priority = params.priority === undefined ? 1 : params.priority;
        this.answerType = params.answerType || '';
        this.options = params.options || null;
    }
}
