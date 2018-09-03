import {Component, OnInit} from '@angular/core';
import {EventTemplate} from '../';
import {ActivatedRoute} from '@angular/router';
import {CheckboxQuestion} from '../questions/question-checkbox';
import {DropdownQuestion} from '../questions/question-dropdown';
import {Question} from '../model/question';
import {UserDropdownQuestion} from '../questions/question-user-dropdown';

@Component({
    selector: 'app-event',
    templateUrl: './event.component.html',
    styleUrls: ['./event.component.css', '../card.css'],
})
export class EventComponent implements OnInit {

    questions: any[] = [];
    eventTemplate: EventTemplate;

    constructor(private actr: ActivatedRoute) { }

    ngOnInit() {
        const locationResolver = this.actr.snapshot.data.locationResolver;
        const questionResolver = this.actr.snapshot.data.questionResolver;
        const userResolver = this.actr.snapshot.data.userResolver;

        // Add a question for Location, and populate it's options with results from locationResolver
        const locOptions = locationResolver._embedded.locations.map(location => location.name);
        const locParams = {
            id: 'location',
            question: 'Location',
            options: locOptions,
            required: true
        }
        this.questions.push(new DropdownQuestion(locParams));

        // Add a question for Subject, and populate it's options with results from userResolver
        const subjectOptions = userResolver._embedded.users.map(user => {
            return { value: user._links.self.href, name: user.lastName.concat(', ').concat(user.firstName) };
        });
        const subjectParams = {
            id: 'subject',
            question: 'Person',
            options: subjectOptions,
            required: true
        }
        this.questions.push(new UserDropdownQuestion(subjectParams));

        // Populate the dynamic questions
        questionResolver._embedded.questions
            .sort((a, b) => a.priority - b.priority)
            .forEach((q: Question) => {
                const params = {
                    id: q._links.question.href,
                    question: q.question,
                    options: q.options,
                    required: q.required,
                    priority: q.priority
                };

                if (q.answerType === 'options') {
                    this.questions.push(new DropdownQuestion(params));
                } else if (q.answerType === 'boolean') {
                    params.required = false;
                    this.questions.push(new CheckboxQuestion(params));
                }
            });
    }

}
