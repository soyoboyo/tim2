export class Translation {
    id: number;

    content: string;
    locale: string;
    updateDate: Date;

    messageId: number;

    constructor(id: number, content: string, locale: string, updateDate: Date, messageId: number) {
        this.id = id;
        this.content = content;
        this.locale = locale;
        this.updateDate = updateDate;
        this.messageId = messageId;
    }
}
