<template>
  <v-app id="inspire">
    <app-bar />
    <v-main class="fundo">
      <router-view />
    </v-main>
  </v-app>
</template>

<script>
  import AppBar from './components/AppBar.vue'
  import { getUsuario } from './services/UsuarioService.js';

  export default {
    name:'App',
    components:{
      AppBar
    },
    data(){
      return{
        usuario: null
      }
    },
    created(){
      this.usuario = getUsuario()

      this.$store.dispatch("usuario", this.usuario)
    }
  }
</script>