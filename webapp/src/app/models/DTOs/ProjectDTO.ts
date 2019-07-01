export class ProjectDTO {
    name: string;
    sourceLocale: string;
    targetLocales: string[];
    replaceableLocaleToItsSubstitute: Map<string, string>;

    constructor(name?: string, sourceLocale?: string, targetLocales?: string[], replaceableLocaleToItsSubstitute?: Map<string, string>) {
        this.name = name;
        this.sourceLocale = sourceLocale;
        this.targetLocales = targetLocales;
        this.replaceableLocaleToItsSubstitute = replaceableLocaleToItsSubstitute;
    }
}
