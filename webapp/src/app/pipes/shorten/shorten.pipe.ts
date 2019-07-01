import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
	name: 'shorten'
})
export class ShortenPipe implements PipeTransform {

	/**
	 * Returns string shortened to given characters.
	 *
	 * @param value - string to be shortened
	 * @param args[0] - number of characters to be displayed
	 * @param args[1] - trail ending shortened string
	 *
	 * @return substring of specified length
	 */
	transform(value: string, args: any[]): string {
		const limit = args.length > 0 ? parseInt(args[0], 10) : 20;
		const trail = args.length > 1 ? args[1] : '...';
		return value.length > limit ? value.substring(0, limit) + trail : value;
	}

}
