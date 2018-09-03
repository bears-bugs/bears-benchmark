/**
 * Part of the side navigation menu. Each NavItem can either be clicked to go to a new page or it has a list
 * of subItems, which can be clicked to go to a new page.
 */
export class NavItem {
    display: string;
    link: string;
    showSubItems: boolean;
    subItems: NavItem[];
    constructor(display: string, link: string) {
        this.display = display;
        this.link = link;
        this.showSubItems = false;
        this.subItems = undefined;
    }

    /**
     * @returns true if the NavItem or one of its SubItems are currently selected.
     */
    public isCurrentlySelected(locationPath: string) {
        if (this.link === locationPath) {
            return true;
        }

        if (this.subItems) {
            for (const subItem of this.subItems) {
                if (subItem.isCurrentlySelected(locationPath)) {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * Constructor if your NavItem has sub items.
     */
    public static createNavItemWithSubItems(display: string, subItems: NavItem[]): NavItem {
        const navItem = new NavItem(display, undefined);
        navItem.subItems = subItems;
        return navItem;
    }
}
