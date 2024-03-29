import {Component} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {ActivatedRoute, NavigationEnd, Router} from "@angular/router";
import { faChevronRight } from '@fortawesome/free-solid-svg-icons';
import {Order, Pizza} from "./types";
import {interval} from 'rxjs'

@Component({
    selector: 'app-root',
    templateUrl: './app.component.html',
    styleUrls: ['./app.component.css']
})
export class AppComponent {
    private selectedId?: number;

    constructor(private http: HttpClient, private route: ActivatedRoute, private router: Router) {

    }
    icons = {
        faChevronRight
    }

    message: string = '';
    orders: Order[] = [];
    pizzas: Pizza[] = [];

    // Display mode
    mode: string = '';

    ngOnInit() {
        this.refreshPizzasAndOrder();
        interval(1000)
            .subscribe(() => {
                this.refreshPizzasAndOrder();
            });
        // WTF because else component is loaded before routing so queryparams are empty
        this.router.events.subscribe(
            (event) => {
                if (event instanceof NavigationEnd) {
                    this.route.queryParamMap
                        .subscribe(params => {
                            const id = params.get('id');
                            if (id) {
                                this.selectedId = +id;
                            }
                            let paramMode = params.get('mode');
                            if (paramMode) {
                                this.mode = paramMode;
                            } else {
                                this.router.navigate(
                                    [],
                                    {
                                        relativeTo: this.route,
                                        queryParams: {mode: 'pizza'},
                                        queryParamsHandling: 'merge', // remove to replace all query params by provided
                                    });
                            }
                        })
                }
            }
        );

        window.addEventListener('keypress', (event) => {
            if(event.key === 'r') {
                this.ready('QUATRE_FROMAGES')
            } else  if(event.key === 'a') {
                this.ready('CYCLOPE')
            } else  if(event.key === 'o') {
                this.ready('PROVENCAL')
            } else {
                document.getElementById('scan')?.focus();
            }
        })

        this.getRandomGif();
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
        this.http.post(`/api/${pizzaType}/new`, {})
            .subscribe((response: any) => {
                this.refreshPizzasAndOrder();
                this.printBarcode(response.id)
            })
    }

    ready(pizzaType: string) {
        this.http.post(`/api/${pizzaType}/ready`, {})
            .subscribe(() => {
                this.refreshPizzasAndOrder();
            })
    }

    onKey(event: KeyboardEvent) { // without type info
        if (event.key === 'Enter') {
            this.message = '';
            var trimmed = (event.target as HTMLInputElement).value.trim();
            this.http.post(`/api/${trimmed}/delivered`, { })
                .subscribe((res: any) => {
                    this.refreshPizzasAndOrder();
                    this.message = 'Commande n°' + res.id + ' (' + res.pizzaType +') validée';
                    (event.target as HTMLInputElement).value = '';
                    document.body.style.backgroundColor = 'green'
                    setTimeout(() => {
                        document.body.style.backgroundColor = 'unset'
                    }, 1000)
                }, () => {
                    document.body.style.backgroundColor = 'red'
                    setTimeout(() => {
                        document.body.style.backgroundColor = 'unset'
                    }, 1000)
                    this.message = 'Commande n°' + trimmed +' invalide';
                    (event.target as HTMLInputElement).value = '';
                })
        }
    }

    get sortedOrders(): Order[] {
        return this.orders
            .sort((a: Order, b: Order) => a.id > b.id ? 1 : a.id === b.id ? 0 : -1)
            .filter(o => o.status !== 'PICKED_UP')
            .reverse();
    }

    get preparingOrders(): Order[] {
        return this.orders
            .sort((a: Order, b: Order) => a.id > b.id ? 1 : a.id === b.id ? 0 : -1)
            .filter(o => o.status === 'ORDERED');
    }

    get readyOrders(): Order[] {
        return this.orders
            .sort((a: Order, b: Order) => a.id > b.id ? 1 : a.id === b.id ? 0 : -1)
            .filter(o => o.status === 'DELIVERED');
    }

    get selectedOrder(): Order | undefined {
        if (!this.selectedId) {
            return undefined;
        } else {
            return this.orders.find(o => o.id === this.selectedId);
        }
    }

    get hourOfSelectedOrder(): string {
        if(!this.selectedOrder) {
            return '';
        }
        var date = new Date(this.selectedOrder.createdAt);
        return (date.getHours() + 2).toString().padStart(2, '0') + ':' + date.getMinutes().toString().padStart(2, '0');
    }

    get numberOfOrderInFront(): number {
        if(!this.selectedOrder) {
            return 0;
        }
        return this.preparingOrders.indexOf(this.selectedOrder);
    }


    getRandomGif() {
        // Giphy API defaults
        const giphy = {
            baseURL: "https://api.giphy.com/v1/gifs/",
            apiKey: "0UTRbFtkMxAplrohufYco5IY74U8hOes",
            tag: "pizza",
            type: "random",
            rating: "g"
        };
        // Target gif-wrap container
        // Giphy API URL
        let giphyURL = encodeURI(
            giphy.baseURL +
            giphy.type +
            "?api_key=" +
            giphy.apiKey +
            "&tag=" +
            giphy.tag +
            "&rating=" +
            giphy.rating
        );

        // Call Giphy API and render data
        this.http.get(giphyURL).subscribe((giphy: any) => {
            const gifdiv = document.getElementById('gif-wrap') as HTMLImageElement;
            if (gifdiv) {
                gifdiv.src = giphy.data.image_original_url
            }
        })
    }

    printBarcode(id: string | number) {
        const Pagelink = "about:blank";
        const pwa = window.open(Pagelink, "_new");
        pwa?.document.open();
        pwa?.document.write(`<html><head><script>function step1(){
setTimeout('step2()', 10);}
function step2(){window.print();window.close()}
</script></head><body onload='step1()'>
<img src='/barcode?id=${id}' /></body></html>`);
        pwa?.document.close();
    }

    goTo(dest: string) {
        this.router.navigate(
            [],
            {
                relativeTo: this.route,
                queryParams: {mode: dest},
                queryParamsHandling: 'merge', // remove to replace all query params by provided
            });
    }
}
