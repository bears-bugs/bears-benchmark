/**
 * Config file to switch between different types of applications. Ex. Hand Hygiene, Blue Gloves, or Blue Masks.
 */
export class Config {
    servicesReturnFakeData = false;

    appName: string;
    websiteUrl: string;
    bannerURL: string;
    logoURL: string;
    backendURL: string;
    backendPort: number;

    constructor(appName: string,
                websiteUrl: string,
                bannerURL: string,
                logoURL: string,
                backendURL: string,
                backendPort: number) {
        this.appName = appName;
        this.websiteUrl = websiteUrl;
        this.bannerURL = bannerURL;
        this.logoURL = logoURL;
        this.backendURL = backendURL;
        this.backendPort = backendPort;
    }

    public setConfig(config: Config) {
        this.appName = config.appName;
        this.websiteUrl = config.websiteUrl;
        this.bannerURL = config.bannerURL;
        this.logoURL = config.logoURL;
        this.backendURL = config.backendURL;
        this.backendPort = config.backendPort;
    }
}
