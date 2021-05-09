import '@feathersjs/transport-commons';
import { HookContext } from '@feathersjs/feathers';
import { Application } from './declarations';

export default function(app: Application): void {
  app.publish(data => app.channel('everybody'));
}
