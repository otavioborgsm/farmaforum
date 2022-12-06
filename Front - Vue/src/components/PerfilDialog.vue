<template>
    <v-dialog v-model="dialog"
      persistent max-width="600"
      @click:outside="clickCancelar()"
    >
      <v-card shaped color="fundo">
        <v-toolbar flat color="verdeClaro" dark>

          <v-avatar v-if="!passo">
            <v-icon class="mr-2">mdi-account</v-icon>
          </v-avatar>
          <v-btn icon v-else-if="passo" @click="passo = ''"
            :elevation="0" class="mr-2">
            <v-icon>mdi-arrow-left</v-icon>
          </v-btn>

          <v-spacer></v-spacer>
          <v-toolbar-title>
            <span class="font-weight-light">Perfil </span> 
            <b>FarmaForum</b>
          </v-toolbar-title>
          <v-spacer></v-spacer>

          <v-btn color="verdeEscuro" fab small @click="clickCancelar">
            <v-icon>
              mdi-close
            </v-icon>
          </v-btn>

        </v-toolbar>
        <v-card-text class="mt-2">

          <v-container v-if="!passo">

            <v-btn block elevation="0" color="verdeClaro" dark @click="buscarUsuarioParaEdicao" 
              :loading="carregando">
                <v-icon>
                  mdi-pencil
                </v-icon>
                <v-spacer>Editar Perfil</v-spacer>
            </v-btn>

            <v-btn block elevation="0" color="verdeClaro" dark @click="passo = 'FARMACEUTICO'"
              v-if="!usuario.farmaceutico" class="mt-5">
                <v-icon>
                  mdi-pill
                </v-icon>
                <v-spacer>Sou Farmacêutico</v-spacer>
            </v-btn>

            <v-btn block elevation="0" color="verdeMedio" dark @click="usuarioLogout" class="mt-5">
                <v-icon>
                  mdi-logout-variant
                </v-icon>
                <v-spacer>Sair</v-spacer>
            </v-btn>

          </v-container>

          <v-container v-else-if="passo === 'FARMACEUTICO'">
            <v-form v-model="valido">
              <h6 tile class="text-h6 mb-2">Informe seu Estado e CRF para validação das credenciais:</h6>
              <v-text-field class="pt-3" label="CRF" color="verdeClaro" outlined
                  placeholder="Informe seu CRF" :rules="[required('CRF')]"
                  v-model="crf">
              </v-text-field>

              <v-select :items="estados" label="Estado" color="verdeClaro"
                outlined v-model="estado">
              </v-select>

              <v-btn block elevation="0" color="verdeClaro" dark @click="solicitarValidacaoFarmaceutico"
                :disabled="!valido" :loading="carregando">
                Enviar
              </v-btn>
            </v-form>
          </v-container>

          <v-container v-else-if="passo === 'EDICAO'">
            <v-form v-model="formularioEdicao">
              <h6 tile class="text-h6 mb-2">Editar usuário:</h6>

              <v-text-field class="pt-3" label="Nome" color="verdeClaro"
                  placeholder="Informe seu Nome" outlined v-model="usuarioEdicao.nome"
                  type="text" :rules="[required('Nome')]">
              </v-text-field>

              <v-text-field label="Sobrenome" color="verdeClaro" outlined :rules="[required('Sobrenome')]"
                  placeholder="Informe seu Sobrenome" v-model="usuarioEdicao.sobrenome" type="text">
              </v-text-field>

              <v-btn block elevation="0" color="verdeClaro" dark @click="editarUsuario"
                :disabled="!formularioEdicao" :loading="carregando">
                Enviar
              </v-btn>

            </v-form>
          </v-container>

        </v-card-text>
      </v-card>

      <SnackbarNotificacao v-if="mostrarSnackbarNotificacao" :texto="mensagemSnackbar" :corSucesso="snackbarSucesso"
        @fechaSnackbar="mostrarSnackbarNotificacao = false"/>

    </v-dialog>
</template>

<script>
import { logout } from "../services/UsuarioService.js"
import { mapGetters } from "vuex"
import SnackbarNotificacao from "./utils/SnackbarNotificacao.vue"

  export default {
    name: "LogoutDialog",
    components:{
      SnackbarNotificacao
    },
    data () {
      return {
        dialog: true,
        passo: '',
        crf: '',
        estado: '',
        valido: false,
        carregando: false,
        mostrarSnackbarNotificacao: false,
        mensagemSnackbar: '',
        snackbarSucesso: true,
        estados:['AC','AL','AP','AM','BA','CE','DF','ES','GO','MA','MS','MT',
          'MG','PA','PB','PR','PE','PI','RJ','RN','RS','RO','RR','SC','SP',
          'SE', 'TO'],
        usuarioEdicao: new Object(),
        formularioEdicao: false,
      }
    },

    methods:{
      
      clickCancelar(){
        this.dialog = false
        this.$emit('fechaModal')
      },

      usuarioLogout(){
        logout()
        this.$store.dispatch("usuario", null)
        this.dialog = false
        this.$emit('fechaModal')
      },

      async solicitarValidacaoFarmaceutico(){
        this.carregando = true
        await this.$http.post("/email/validarFarmaceutico/" + this.usuario.id, {
          crf: this.crf,
          estado: this.estado
        }).then(()=>{
            this.mostrarSnackbarNotificacao = true
            this.snackbarSucesso = true
            this.mensagemSnackbar = 'Seus dados foram enviados para análise.'
            this.passo = ''            
        }).catch((err)=>{
            console.log(err)
            this.mostrarSnackbarNotificacao = true
            this.mensagemSnackbar = err.response.data.message
            this.snackbarSucesso = false
        })
        
        this.carregando = false
      },

      async buscarUsuarioParaEdicao(){
        this.carregando = true
        await this.$http.get('/usuarios/' + this.usuario.id)
        .then((res)=>{
          this.usuarioEdicao = res.data
          this.passo = 'EDICAO'            
        }).catch((err)=>{
          console.log(err)
          this.mostrarSnackbarNotificacao = true
          this.mensagemSnackbar = err.response.data.message
          this.snackbarSucesso = false
        })

        this.carregando = false
      },

      async editarUsuario(){
        this.carregando = true
        await this.$http.put("/usuarios/" + this.usuarioEdicao.id, {
          nome: this.usuarioEdicao.nome,
          sobrenome: this.usuarioEdicao.sobrenome
        }).then(()=>{
            this.mostrarSnackbarNotificacao = true
            this.snackbarSucesso = true
            this.mensagemSnackbar = 'Usuário editado com sucesso!'
            this.passo = ''            
        }).catch((err)=>{
            console.log(err)
            this.mostrarSnackbarNotificacao = true
            this.mensagemSnackbar = err.response.data.message
            this.snackbarSucesso = false
        })
        this.carregando= false
      },

      required(propriedade){
        return v =>  v && v.length > 0 || `${propriedade} é um campo necessário`
      },
    },
    computed:{
      ...mapGetters(['usuario'])
    }
  }
</script>


<style scoped>
.spanClicavel{
  cursor: pointer;
  color:#52796F;
}
</style>