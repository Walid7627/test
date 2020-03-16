import { CPV } from './cpv.model';
import { APE } from './ape.model';

export class Segment {
    id: number;
    nom: string;
    codeCPV: CPV;
    codeAPE: APE;
}