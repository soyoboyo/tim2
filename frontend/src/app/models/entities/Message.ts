import { Translation } from './Translation';

export class Message {
    id: number;
    content: string;
    description: string;
    key: string;
    updateDate: Date;
    isRemoved: boolean;

    translations: Translation[];

    constructor(id: number, content: string, description: string, key: string, updateDate: Date, isRemoved: boolean, translations: Translation[]) {
        this.id = id;
        this.content = content;
        this.description = description;
        this.key = key;
        this.updateDate = updateDate;
        this.isRemoved = isRemoved;
        this.translations = translations;
    }
}
