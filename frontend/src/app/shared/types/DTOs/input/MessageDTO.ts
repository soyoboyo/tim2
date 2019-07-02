export class MessageDTO {
	key: string;
	content: string;
	description: string;
	projectId: number;
	translation: string;

	constructor(key: string, content: string, description: string, projectId: number) {
		this.key = key;
		this.content = content;
		this.description = description;
		this.projectId = projectId;
	}
}
