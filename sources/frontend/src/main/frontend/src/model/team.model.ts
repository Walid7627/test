import { Entity } from './entity.model';
import { Address } from './address.model';
import { Purchaser } from './purchaser.model';

export class Team {
    id: number;
    nom: string;
    entite: Entity;
    chef: Purchaser;
}