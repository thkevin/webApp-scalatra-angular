import {Mower} from './mower'


export class FullCampaign {
  id: string;
  name: string;
  datetime: string;
  topX: number;
  topY: number;
  mowers: Mower[];
  surface: boolean[][]
}
