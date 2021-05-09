import '@feathersjs/transport-commons';
import {NullableId} from "@feathersjs/feathers";


type Status =   'ORDERED' | 'DONE';

// This is the interface for the message data
class Pizza {
    constructor(type: string, status: Status, orderDate: Date) {
        this.type = type;
        this.status = status;
        this.orderDate = orderDate;
    }
    id?: number;
    type: string;
    status: Status;
    orderDate: Date;
}

// A messages service that allows to create new
// and return all existing messages
export default class PizzaService {
    pizzas: Pizza[] = [];

    async find () {
        // Just return all our messages
        return this.pizzas;
    }

    async create (data: any) {
        // The new message is the data text with a unique identifier added
        // using the messages length since it changes whenever we add one
        const pizza: Pizza = new Pizza(data.type, 'ORDERED', new Date())

        // Add new message to the list
        // TODO save
        this.pizzas.push(pizza);

        return pizza;
    }

    async patch(id: NullableId, data: any) {
        const pizzaToDeliver = this.pizzas
            .filter(p => p.type === data.type && p.status === 'ORDERED')
            .sort((a, b) => { return b.orderDate.getTime() - a.orderDate.getTime() })[0];

        if(pizzaToDeliver) {
            pizzaToDeliver.status = 'DONE';
        }
        // TODO Resave
        return pizzaToDeliver;
    }
}
