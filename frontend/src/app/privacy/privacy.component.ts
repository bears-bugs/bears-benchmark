import {Component} from '@angular/core';
import {ConfigService} from '../config/config.service';
import {Config} from '../config/config';

@Component({
    selector: 'app-privacy',
    templateUrl: './privacy.component.html',
    styleUrls: ['./privacy.component.css', '../card.css']
})

/**
 * A component for the H2MS Privacy Policy.
 */
export class PrivacyComponent {
    config: Config;

    constructor(private configService: ConfigService) {
        this.config = configService.getConfig();
    }
}
