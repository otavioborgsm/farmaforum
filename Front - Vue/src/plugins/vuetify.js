import Vue from 'vue';
import Vuetify from 'vuetify/lib/framework';

Vue.use(Vuetify);

export default new Vuetify({
    theme: {
        themes: {
            light:{
                fundo: "#DAD7CD",
                fundoClaro: "#F0ECD5",
                verdeClaro: "#84A98C",
                verdeMedio: "#52796F",
                verdeEscuro: "#354F52",
                azulEscuro: "#2F3E46"
            }
        }
    }
});
