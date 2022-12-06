<template>
  <v-container>
    <v-card color="fundoClaro" class="verdeEscuro--text">
      <v-container>

        <h4 class="text-h4 my-3">Criar uma nova publicação:</h4>
        <v-divider></v-divider>

        <v-form ref="formulario" v-model="valido">

          <v-text-field class="pt-7" label="Titulo da publicação" color="verdeClaro"
            placeholder="Titulo" outlined v-model="publicacao.titulo"
            type="text" :rules="[required('Titulo'), tamanhoMaximo('Titulo', 150)]">
          </v-text-field>

          <v-combobox multiple v-model="publicacao.farmacos"  label="Farmacos da publicação" append-icon
            chips deletable-chips item-color="verdeClaro" color="verdeClaro" placeholder="Pressione 'enter' para adicionar." 
            outlined hint="São os Remédios/Químicos que estão presentes no post." persistent-hint>
          </v-combobox>

          <v-textarea label="Conteúdo da publicação" color="verdeClaro" placeholder="Conteúdo"
            outlined v-model="publicacao.conteudo" :rules="[required('Conteúdo')]"
            class="mt-5"></v-textarea>  
          
          <v-btn block elevation="0" color="verdeClaro" dark @click="publicar"
          :disabled="!valido" :loading="carregando">
            Publicar
          </v-btn>

        </v-form>
      </v-container>
    </v-card>

    <SnackbarNotificacao v-if="mostrarSnackbarNotificacao" :texto="mensagemSnackbar" :corSucesso="snackbarSucesso"
      @fechaSnackbar="mostrarSnackbarNotificacao = false"/>

  </v-container>
</template>

<script>
import SnackbarNotificacao from "@/components/utils/SnackbarNotificacao.vue"
import { mapGetters } from 'vuex'

export default {
    name: 'novaPublicacao',
    components:{
      SnackbarNotificacao
    },
    data(){
      return{
        valido: false,

        publicacao: {
          titulo: '',
          conteudo: '',
          farmacos: [],
        },
        mostrarSnackbarNotificacao: false,
        mensagemSnackbar: '',
        snackbarSucesso: true,
        carregando: false
      }
    },
    methods:{

      async publicar(){
        this.carregando = true

        let novaPublicacao = {
          titulo: this.publicacao.titulo,
          conteudo: this.publicacao.conteudo,
          idUsuario: this.usuario.id,
          farmacos: this.montaFarmacos()
        }

        await this.$http.post('/publicacoes', novaPublicacao)
        .then((res)=>{
          this.$router.push('/publicacao/' + res.data.id)
        }).catch((err)=>{
          this.mostrarSnackbarNotificacao = true
          this.mensagemSnackbar = err.response.data.message
          this.snackbarSucesso = false
        })

        this.carregando = false
        console.log(this.montaFarmacos())
      },

      montaFarmacos(){
        let farmacos = []

        this.publicacao.farmacos.forEach((item)=>{
          farmacos.push({
            farmaco:{
              descricao: item
            }
          })
        })

        return farmacos
      },

      tamanhoMinimo(propriedade, tamanho){
        return v =>  v && v.length >= tamanho || `${propriedade} precisa de ${tamanho} caracteres.`
      },
      required(propriedade){
        return v =>  v && v.length > 0 || `${propriedade} é um campo necessário.`
      },
      tamanhoMaximo(propriedade, tamanho){
        return v =>  v && v.length <= tamanho || `${tamanho} é o tamanho máximo de ${propriedade}.`
      },
    },
    computed:{
      ...mapGetters(['usuario'])
    }
}
</script>

<style>

</style>