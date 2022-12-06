<template>
  <div>
    <center>
      <v-container class="verdeEscuro--text">
        <v-img class="mt-16 pt-10 mx-6" max-width="550" src="@/assets/logoGrande.png" contain></v-img>
        <h6 class="text-h6 font-weight-bold mb-6">Fórum dedicado a solucionar dúvidas sobre medicamentos.</h6>
        <TextFieldPesquisa @clickEnter="pesquisar" @clickPesquisar="pesquisar" v-model="pesquisa"/>
        <h6 class="text-h6 font-weight-bold mb-3">ou</h6>
        <div @click="!usuario ? mostrarLoginDialog = true : ''">
          <router-link class="verdeEscuro--text text-h6 font-weight-bold" :to="usuario? '/novaPublicacao' : ''">Publique a sua dúvida.</router-link>
        </div>
      </v-container>
    </center>

    <LoginDialog v-if="mostrarLoginDialog" @fechaModal="mostrarLoginDialog = false" />
  </div>
</template>

<script>
import { mapGetters } from 'vuex'
import LoginDialog from "@/components/LoginDialog.vue"
import TextFieldPesquisa from "@/components/utils/TextFieldPesquisa.vue"

export default{
  name:'TelaInicio',
  components:{
    LoginDialog,
    TextFieldPesquisa
  },
  data(){
    return{
      mostrarLoginDialog: false,
      pesquisa: ''
    }
  },
  methods:{
    pesquisar(){
      localStorage.setItem('pesquisa', this.pesquisa)
      this.$router.push('/pesquisa')
    }
  },
  computed:{
    ...mapGetters(['usuario'])
  }

}
</script>
