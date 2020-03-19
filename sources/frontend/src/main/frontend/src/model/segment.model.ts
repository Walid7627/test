import { CPV } from './cpv.model';
import { APE } from './ape.model';

export class Segment {
    id: number;
    libelle: string;
    codeCPV: string;
    codeAPE: string;
    
    constructor(libelle : string, codeCPV : string, codeape: string){
        this.libelle=libelle;
        this.codeCPV=codeCPV;
        this.codeAPE=codeape;
        
    }
    
}