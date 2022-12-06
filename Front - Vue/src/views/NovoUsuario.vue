<template>
  <v-container>
      <v-card color="fundoClaro" class="verdeEscuro--text">
          <v-container>
              <h4 class="text-h4 my-3">Criar uma nova conta:</h4>
              <v-divider></v-divider>

              <v-form ref="formulario" v-model="valido">

                <v-text-field class="pt-7" label="Nome" color="verdeClaro"
                    placeholder="Informe seu Nome" outlined v-model="nome"
                    type="text" :rules="[required('Nome')]">
                </v-text-field>

                <v-text-field label="Sobrenome" color="verdeClaro" outlined :rules="[required('Sobrenome')]"
                    placeholder="Informe seu Sobrenome" v-model="sobrenome" type="text">
                </v-text-field>

                <v-text-field label="E-mail" color="verdeClaro" outlined :rules="[required('E-mail')]"
                    placeholder="Informe seu E-mail" v-model="email" type="email">
                </v-text-field>

                <v-text-field label="Senha" color="verdeClaro" placeholder="Crie uma senha"
                    outlined v-model="senha" type="password" :rules="[required('Senha'), tamanhoMinimo('Senha', 8)]">
                </v-text-field>

                <v-text-field label="Confirmar Senha" color="verdeClaro" placeholder="Confirme sua senha"
                    outlined v-model="confirmarSenha" type="password" :rules="[required('Confirmar Senha'), validaSenha()]">
                </v-text-field>

                <v-btn block elevation="0" color="verdeClaro" dark @click="cadastrarUsuario"
                    :disabled="!valido" :loading="carregando">
                    Cadastrar
                </v-btn>
              </v-form>
          </v-container>
      </v-card>

      <SnackbarNotificacao v-if="mostrarSnackbarNotificacao" :texto="mensagemSnackbar" :corSucesso="snackbarSucesso"
        @fechaSnackbar="mostrarSnackbarNotificacao = false"/>

        <v-dialog v-model="dialog"
        persistent max-width="600">
            <v-card shaped color="fundo">
                <v-toolbar flat color="verdeClaro" dark>
                <v-avatar>
                    <v-icon class="mr-2">mdi-account</v-icon>
                </v-avatar>
                <v-spacer></v-spacer>
                <v-toolbar-title>
                    <span class="font-weight-light">Validar E-mail</span> 
                </v-toolbar-title>
                <v-spacer></v-spacer>
                <v-btn color="verdeEscuro" fab small @click="dialog = false">
                    <v-icon>
                        mdi-close
                    </v-icon>
                </v-btn>
                </v-toolbar>

                <v-card-text class="mt-2">
                    <v-container>
                        <v-form ref="formularioValidacao" v-model="validacao">
                            <h6 tile class="text-h6 mb-2">Foi enviado um token para o E-mail: {{ email }}</h6>
                            <v-text-field label="Token" color="verdeClaro" outlined :rules="[required('Token')]"
                                placeholder="Informe o token de validação" v-model="tokenValidacao" type="text">
                            </v-text-field>
                            <v-btn block elevation="0" color="verdeClaro" dark :loading="carregando"
                                @click="validarEmail" :disabled="!validacao">
                                <v-icon left>
                                    mdi-send
                                </v-icon>
                                    Validar
                            </v-btn>
                        </v-form>
                    </v-container>
                </v-card-text>
            </v-card>
    </v-dialog>
  </v-container>
</template>

<script>
import SnackbarNotificacao from "@/components/utils/SnackbarNotificacao.vue"


export default {
    name: 'NovoUsuario',
    components:{
      SnackbarNotificacao
    },
    data(){
        return{
            nome: '',
            sobrenome: '',
            email: '',
            senha: '',
            confirmarSenha: '',
            valido: false,
            mostrarSnackbarNotificacao: false,
            mensagemSnackbar: '',
            snackbarSucesso: true,
            dialog: false,
            tokenValidacao: '',
            validacao: false,
            carregando: false
        }
    },
    methods:{
        async cadastrarUsuario(){
            this.carregando = true
            const data = {
                login: this.email,
                nome: this.nome,
                senha: this.senha,
                sobrenome: this.sobrenome,
            }

            await this.$http.post("/usuarios", data)
            .then(()=>{
                this.mostrarSnackbarNotificacao = true
                this.mensagemSnackbar = 'Foi enviado um token de validação para seu E-mail'
                this.snackbarSucesso = true
                this.dialog = true
            })
            .catch((err)=>{
                this.mostrarSnackbarNotificacao = true
                this.mensagemSnackbar = err.code === "ERR_BAD_REQUEST" ? "E-mail já está em uso!" : err.response.data.message
                this.snackbarSucesso = false
            })
            this.carregando = false
        },

        async validarEmail(){
            this.carregando = true
            await this.$http.get('/email/verificar?token=' + this.tokenValidacao)
            .then((res)=>{
                console.log(res)
                this.mostrarSnackbarNotificacao = true
                this.snackbarSucesso = true
                this.mensagemSnackbar = 'E-mail validado com sucesso! Usuário cadastrado!'
            }).finally(()=>{
                setTimeout(()=>{
                    this.carregando = false
                    this.dialog = false
                    this.$router.go(-1)
                }, 2000)
            })
            .catch((err)=>{
                console.log(err)
                this.mostrarSnackbarNotificacao = true
                this.mensagemSnackbar = err.response.data.message
                this.snackbarSucesso = false
            })
        },

        tamanhoMinimo(propriedade, tamanho){
            return v =>  v && v.length >= tamanho || `${propriedade} precisa de ${tamanho} caracteres.`
        },
        required(propriedade){
            return v =>  v && v.length > 0 || `${propriedade} é um campo necessário`
        },
        validaSenha(){
            return v =>  v && v === this.senha || `Senhas não batem!`
        }
    }
}
</script>

<style scoped>
h4{
    color: #354F52;
}

</style>