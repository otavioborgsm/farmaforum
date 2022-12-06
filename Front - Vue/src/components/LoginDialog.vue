<template>
    <v-dialog v-model="dialog"
      persistent max-width="600"
      @click:outside="clickCancelar()"
    >
      <v-card shaped color="fundo">
        <v-toolbar flat color="verdeClaro" dark>

          <v-avatar v-if="passo === 1">
            <v-icon class="mr-2">mdi-account</v-icon>
          </v-avatar>
          <v-btn icon v-else-if="passo === 2" @click="passo -= 1"
            :elevation="0" class="mr-2">
            <v-icon>mdi-arrow-left</v-icon>
          </v-btn>
          <v-avatar v-else>
            <v-icon class="mr-2">mdi-key-variant</v-icon>
          </v-avatar>

          <v-spacer></v-spacer>
          <v-toolbar-title v-if="passo === 1">
            <span class="font-weight-light">Login </span> 
            <b>FarmaForum</b>
          </v-toolbar-title>

          <v-toolbar-title v-else>
            <span class="font-weight-light">Recuperar Senha</span> 
          </v-toolbar-title>
          <v-spacer></v-spacer>

          <v-btn color="verdeEscuro" fab small @click="clickCancelar">
            <v-icon>
              mdi-close
            </v-icon>
          </v-btn>
        </v-toolbar>

        <v-card-text class="mt-2">

          <v-container v-if="passo === 1">
            <v-form v-model="formularioLogin">
              <v-text-field class="pt-3" label="E-mail" color="verdeClaro"
                  placeholder="Informe seu E-mail" outlined v-model="email"
                  type="email" :rules="[required('E-mail')]">
              </v-text-field>

              <v-text-field label="Senha" color="verdeClaro" placeholder="Informe sua Senha"
                  outlined v-model="senha" type="password" :rules="[required('Senha'), tamanhoMinimo('Senha', 8)]">
              </v-text-field>

              <v-btn block :elevation="2" color="verdeClaro" dark @click="login" :disabled="!formularioLogin">
                Enviar
              </v-btn>
            </v-form>
            <div class="text-center mt-3">
              <span class="spanClicavel" @click="passo = 2">Esqueci minha senha.</span>
            </div>
            <p class="mt-3 text-center"> Ou então: 
              <v-hover> 
                <router-link class="spanClicavel" @click.native="clickCancelar" to="/criarConta" >crie sua conta.</router-link>
              </v-hover>
            </p>
          </v-container>

          <v-container v-else-if="passo === 2">
            <v-form v-model="formularioEmail">
              <h6 tile class="text-h6 mb-2">Informe seu e-mail para receber um token de validação.</h6>
              <v-text-field class="pt-3" label="E-mail" color="verdeClaro" outlined
                  placeholder="Informe seu E-mail" :rules="[required('E-mail')]"
                  v-model="emailRecuperacao" type="email">
              </v-text-field>
              <v-btn block elevation="0" color="verdeClaro" dark @click="recuperarSenha"
                :disabled="!formularioEmail" :loading="carregando">
                Enviar
              </v-btn>
              <div class="text-center mt-3">
                <span class="spanClicavel" @click="passo = 3">Já possui um token.</span>
              </div>
            </v-form>
          </v-container>

          <v-container v-else-if="passo === 3">
            <v-form v-model="formularioNovaSenha">
              <v-text-field label="Token" color="verdeClaro" placeholder="Informe o Token recebido:"
                  outlined v-model="token" type="text" :rules="[required('Token')]">
              </v-text-field>
              <v-text-field label="Nova Senha" color="verdeClaro" placeholder="Informe sua nova senha"
                  outlined v-model="novaSenha" type="password" :rules="[required('Nova Senha'), tamanhoMinimo('Nova Senha', 8)]">
              </v-text-field>
              <v-text-field label="Confirmar Senha" color="verdeClaro" placeholder="Confirme sua senha"
                  outlined v-model="confirmarSenha" type="password" :rules="[validaSenha()]">
              </v-text-field>
              <v-btn block elevation="0" color="verdeClaro" dark @click="alterarSenha"
                :disabled="!formularioNovaSenha" :loading="carregando">
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
import SnackbarNotificacao from "./utils/SnackbarNotificacao.vue"
import { login } from "../services/UsuarioService.js"

  export default {

    name: "LoginDialog",

    components:{
      SnackbarNotificacao
    },

    data () {
      return {
        dialog: true,
        passo: 1,
        formularioLogin: false,
        formularioEmail: false,
        formularioNovaSenha: false,
        email: null,
        senha: null,
        emailRecuperacao: null,
        token: null,
        novaSenha: null,
        confirmarSenha: null,
        carregando: false,
        mostrarSnackbarNotificacao: false,
        mensagemSnackbar: '',
        snackbarSucesso: true
      }
    },

    methods:{
      
      clickCancelar(){
        this.dialog = false
        this.$emit('fechaModal')
      },

      async login(){
        await this.$http.post("/usuarios/auth",{
          login: this.email,
          senha: this.senha
        }).then((res) =>{
          const usuario = res.data
          console.log(usuario)

          this.$store.dispatch("usuario", usuario)
          login(usuario)
        }).finally(()=>{  
          this.clickCancelar()
        }).catch((err)=>{
          console.log(err)
          this.mostrarSnackbarNotificacao = true
          this.mensagemSnackbar = "Usuário ou senha inválidos!"
          this.snackbarSucesso = false
        })
      },

      async recuperarSenha(){
        this.carregando = true
        await this.$http.post("/email/senha/requisicao",{
          email: this.emailRecuperacao
        }).then((res) =>{
          console.log(res)
          this.mostrarSnackbarNotificacao = true
          this.mensagemSnackbar = 'Token enviado com sucesso!'
          this.snackbarSucesso = true

          this.carregando = false
          this.passo = 3
        }).catch(err =>{
          console.log(err)
          this.mostrarSnackbarNotificacao = true
          this.mensagemSnackbar = err.code === "ERR_BAD_REQUEST" ? "E-mail inválido!" : err.response.data.message
          this.snackbarSucesso = false
        })
        this.carregando = false
      },

      async alterarSenha(){
        this.carregando = true
        await this.$http.put("/email/senha/atualizar",{
          senha: this.novaSenha,
          token: this.token
        }).then((response)=>{
          this.mostrarSnackbarNotificacao = true
          this.mensagemSnackbar = 'Senha alterada com sucesso!'
          this.snackbarSucesso = true

          console.log(response)
          this.carregando = false
          this.passo = 1
        }).catch(err =>{
          this.mostrarSnackbarNotificacao = true
          this.mensagemSnackbar = err.response.data.message
          this.snackbarSucesso = false
        })
        this.carregando = false
      },

      tamanhoMinimo(propriedade, tamanho){
        return v =>  v && v.length >= tamanho || `${propriedade} precisa de ${tamanho} caracteres.`
      },
      required(propriedade){
        return v =>  v && v.length > 0 || `${propriedade} é um campo necessário`
      },
      validaSenha(){
        return v =>  v && v === this.novaSenha || `Senhas não batem!`
      }
    }
  }
</script>


<style scoped>
.spanClicavel{
  cursor: pointer;
  color:#52796F;
}
</style>