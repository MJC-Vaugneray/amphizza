import { Application } from '../declarations';
// Don't remove this comment. It's needed to format import lines nicely.
import PizzaService from './PizzaService';

export default function (app: Application): void {
    app.use('/pizzas', new PizzaService());


    app.on('connection', connection =>
        app.channel('everybody').join(connection)
    );

    // app.service('pizzas').create({
    //     text: 'Hello world from the server'
    // });

    // // Log every time a new message has been created
    // app.service('pizzas').on('created', (message: string) => {
    //     console.log('A new message has been created', message);
    // });
}
