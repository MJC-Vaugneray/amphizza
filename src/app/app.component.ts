import {Component} from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';

@Component({
    selector: 'app-root',
    templateUrl: './app.component.html',
    styleUrls: ['./app.component.css']
})
export class AppComponent {
    constructor(private http: HttpClient) {

    }

    title = 'client';
    orders: any;
    pizzas: any;

    ngOnInit() {
        this.refreshPizzasAndOrder();
    }

    private refreshPizzasAndOrder() {
        this.http.get<any[]>('/orders')
            .subscribe(orders => {
                this.orders = orders;
            })

        this.http.get<any[]>('/pizzas')
            .subscribe(pizzas => {
                this.pizzas = pizzas;
            })
    }

    order(pizzaType: string) {
        this.http.post(`/${pizzaType}/new`, {})
            .subscribe(() => {
                this.refreshPizzasAndOrder();
            })
    }

    ready(pizzaType: string) {
        this.http.post(`/${pizzaType}/ready`, {})
            .subscribe(() => {
                this.refreshPizzasAndOrder();
            })
    }

    onKey(event: KeyboardEvent) { // without type info
        if(event.key === 'Enter') {
            this.http.post(`/${(event.target as HTMLInputElement).value.trim()}/delivered`, {})
                .subscribe(() => {
                    this.refreshPizzasAndOrder();
                    (event.target as HTMLInputElement).value = '';
                })
        }
    }


    printBarcode(source: string) {
        const Pagelink = "about:blank";
        const pwa = window.open(Pagelink, "_new");
        pwa?.document.open();
        pwa?.document.write(`<html><head><script>function step1(){
setTimeout('step2()', 10);}
function step2(){window.print();window.close()}
</script></head><body onload='step1()'>
<img src='${source}' /></body></html>`);
        pwa?.document.close();
    }
}
