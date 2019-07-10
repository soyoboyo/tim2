export class AggregatedInfoForDeveloper {
	projectId: number;
	translationStatusesByLocale: Map<string, Map<string, number>>;
	messagesTotal: number;
}
