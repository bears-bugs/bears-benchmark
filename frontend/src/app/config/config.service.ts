import {Injectable} from '@angular/core';
import {Config} from './config';
import * as h2ms from './h2ms-config';
import * as gloves from './gloves-config';

/**
 * The config service provides a Config file.
 */
@Injectable()
export class ConfigService {
    useH2MSConfig = true;
    config: Config;

    constructor() {
        this.config = new Config(h2ms.CONFIG.appName,
            h2ms.CONFIG.websiteUrl,
            h2ms.CONFIG.bannerURL,
            h2ms.CONFIG.logoURL,
            h2ms.CONFIG.backendURL,
            h2ms.CONFIG.backendPort);
    }

    getConfig(): Config {
        this.updateConfig();
        return this.config;
    }

    /**
     * Toggle between different configs. H2MS and Blue Gloves initially.
     *
     * This is accomplished by switching out the active configs internal state.
     */
    toggleConfig(): void {
        this.useH2MSConfig = !this.useH2MSConfig;
        this.updateConfig();
    }

    private updateConfig() {
        if (this.useH2MSConfig) {
            this.config.setConfig(h2ms.CONFIG);
        } else {
            this.config.setConfig(gloves.CONFIG);
        }
    }
}
