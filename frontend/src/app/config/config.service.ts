import {Injectable} from '@angular/core';
import {Config} from './config';
import * as h2ms from './h2ms-config';
import * as h2msLocal from './local-h2ms-config';
import * as glovesLocal from './local-h2ms-config';
import * as gloves from './gloves-config';

/**
 * The config service provides a Config file.
 */
@Injectable()
export class ConfigService {
    useH2MSConfig = true;
    useLocalConfig = false;
    config: Config;

    constructor() {
        const defaultConfig = h2ms.CONFIG;
        this.config = new Config(defaultConfig.appName,
            defaultConfig.frontendHostname,
            defaultConfig.frontendPort,
            defaultConfig.bannerUrl,
            defaultConfig.logoUrl,
            defaultConfig.backendHostname,
            defaultConfig.backendPort);
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
        if (this.useLocalConfig && this.useH2MSConfig) {
            this.config.setConfig(h2msLocal.CONFIG);
        } else if (this.useLocalConfig) {
            this.config.setConfig(glovesLocal.CONFIG);
        } else if (this.useH2MSConfig) {
            this.config.setConfig(h2ms.CONFIG);
        } else {
            this.config.setConfig(gloves.CONFIG);
        }
    }
}
