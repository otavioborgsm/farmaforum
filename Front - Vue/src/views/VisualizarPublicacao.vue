<template>
  <v-container>
    <center v-if="carregandoPublicacao">
        <v-progress-circular :size="70" :width="7" class="mt-15"
            color="verdeClaro" indeterminate >
        </v-progress-circular>
    </center>
    
    <v-card color="fundoClaro verdeEscuro--text" v-else>
        <div v-if="usuario && +publicacao.usuario.id === +usuario.id">
            <v-card-actions>
                <v-spacer />
                <v-btn color="verdeClaro" text icon v-if="editando"
                    @click="editando = false" :loading="carregando">
                    <v-icon>
                        mdi-pencil-off
                    </v-icon>
                </v-btn>
                <v-btn color="verdeClaro" text icon v-if="!editando"
                    @click="editando = true">
                    <v-icon>
                        mdi-pencil
                    </v-icon>
                </v-btn>
                <v-btn color="verdeClaro" text icon v-else
                    @click="editarPublicacao" :loading="carregando">
                    <v-icon>
                        mdi-send
                    </v-icon>
                </v-btn>
                <v-btn color="verdeClaro" text icon :loading="carregando"
                    @click="dialogFuncao = true; textoDialog = 'Deseja excluir a publicação?'; funcaoDialog = 'DELETAR_PUBLICACAO'">
                    <v-icon>
                        mdi-delete
                    </v-icon>
                </v-btn>
            </v-card-actions>
                
            <v-divider></v-divider>
        </div>
        <v-container>
            <v-row no-gutters>
                <v-col md="12">
                    <h4 class="text-h4 my-3 font-weight-bold" v-if="!editando"> {{ publicacao.titulo }}</h4>
                    <v-textarea class="text-h4 font-weight-bold verdeEscuro--text" v-else 
                        :disabled="!editando" v-model="publicacao.titulo"></v-textarea>
                </v-col>
                <v-col :cols="12" :md="8" v-if="publicacao.usuario">
                    <p> {{ publicacao.usuario.nome }} {{ publicacao.usuario.sobrenome }} </p>
                </v-col>
                <v-col :cols="12" :md="4">
                    <p> {{ publicacao.dataCriacao }} </p>
                </v-col>
            </v-row>
            <v-divider></v-divider>

            <div v-if="publicacao.farmacos" class="mt-3 mb-5">
                <v-chip class="mr-2" v-for="farmacoPublicacao in publicacao.farmacos" :key="farmacoPublicacao.id" dark
                    color="verdeMedio">
                    {{ farmacoPublicacao.farmaco.descricao }} 
                </v-chip>
            </div>

            <p v-if="!editando">{{ publicacao.conteudo }}</p>
            <v-textarea class="verdeEscuro--text" v-else 
                        :disabled="!editando" v-model="publicacao.conteudo"></v-textarea>

            <v-row>
                <v-col :cols="10">
                    <h4 class="text-h4 my-3 font-weight-bold">Comentários: </h4>
                </v-col>
                <v-col :cols="2">
                    <v-btn class="mt-5" color="verdeMedio" depressed x-small @click="adicionarComentario">
                        <v-icon class="fundo--text" small>mdi-plus</v-icon>
                    </v-btn>
                </v-col>
            </v-row>

            <div v-if="publicacao.comentarios">
                <div v-if="publicacao.comentarios.length === 0">
                    <p class="text-center my-5 text-subtitle-2">A publicação ainda não tem comentários.</p>
                </div>
                <div v-else>
                    <div v-for="comentario in publicacao.comentarios" :key="comentario.id">
                        <v-divider class="my-5"></v-divider>
                        <v-row>
                            <v-col cols="12" :md="8" class="font-weight-medium pb-0">
                                {{ comentario.usuario.nome }} {{ comentario.usuario.sobrenome }}
                                <v-badge color="verdeClaro"  icon="mdi-pill" x-small inline
                                    v-if="comentario.usuario.crf">
                                </v-badge>
                            </v-col>
                            <v-col cols="10" :md="3" class="pb-0">
                                <span> {{ comentario.dataCriacao }} </span>
                            </v-col>
                            <v-col cols="2" md="1">
                                <center>
                                    <v-menu offset-y >
                                        <template v-slot:activator="{ on, attrs }">
                                            <v-btn v-bind="attrs" v-on="on"
                                                icon x-small>
                                                <v-icon>mdi-dots-vertical</v-icon>
                                            </v-btn>
                                        </template>
                                        <v-list color="fundoClaro" class="verdeClaro--text">
                                            <v-list-item v-if="usuario && +comentario.usuario.id === +usuario.id"
                                                @click="editarComentario(comentario)">
                                                <v-icon left>mdi-pencil</v-icon>
                                                <v-list-item-title>Editar</v-list-item-title>
                                            </v-list-item>
                                            <v-list-item v-if="usuario && +comentario.usuario.id === +usuario.id"
                                                @click="selecionarComentario(comentario, 'DELETAR')">
                                                <v-icon left>mdi-delete</v-icon>
                                                <v-list-item-title>Deletar</v-list-item-title>
                                            </v-list-item>
                                            <v-list-item v-if="!comentario.refutado"
                                                @click="selecionarComentario(comentario, 'REFUTAR')">
                                                <v-icon left>mdi-alert-outline</v-icon>
                                                <v-list-item-title>Refutar</v-list-item-title>
                                            </v-list-item>
                                            <v-list-item v-else @click="selecionarComentario(comentario, 'REFUTAR')">
                                                <v-icon left>mdi-alert-outline</v-icon>
                                                <v-list-item-title>Cancelar refutado</v-list-item-title>
                                            </v-list-item>
                                        </v-list>
                                    </v-menu>
                                </center>
                            </v-col>
                        </v-row>
                        <div>
                            <v-row v-if="comentario.refutado" class="mt-0">
                                <v-col class="pt-0 mb-2" md="1" cols="1">
                                    <v-tooltip bottom>
                                        <template v-slot:activator="{ on, attrs }">
                                            <center>
                                                <v-icon color="warning" v-bind="attrs" v-on="on">
                                                    mdi-alert-outline
                                                </v-icon>
                                            </center>
                                        </template>
                                        <span>Comentário Refutado!</span>
                                    </v-tooltip>
                                </v-col>
                                <v-col class="pt-0 mb-2 text-decoration-line-through" md="11" cols="11">
                                    {{ comentario.conteudo }}
                                </v-col>
                                
                            </v-row>
                            <p v-else class="mb-0">
                                {{ comentario.conteudo }}
                            </p>
                        </div>
                    </div>
                </div>
            </div>
        </v-container>
    </v-card>

    <v-bottom-sheet v-model="comentarioSheet" persistent>
        <v-sheet class="text-center" height="200px" color="fundoClaro">
            <v-row class="verdeClaro">
                <v-col cols="8" md="10">
                    <h6 tile class="text-h6 mb-2 fundoClaro--text text-start ml-5 mt-1">{{ textoSheet }}</h6>
                </v-col>
                <v-col cols="4" md="2">
                    <center>
                        <v-btn color="verdeEscuro" fab x-small @click="comentarioSheet = !comentarioSheet"
                            class="mt-1">
                            <v-icon class="fundoClaro--text" small>mdi-close</v-icon>
                        </v-btn>
                    </center>
                </v-col>
            </v-row>
            <v-container>
                <v-form>
                    <v-text-field class="mt-5" label="Comentário" color="verdeClaro" :loading="carregando"
                        placeholder="Digite seu comentário." outlined v-model="novoComentario" v-if="comentarioSelecionado.id"
                        :rules="[required('Comentário')]" append-icon="mdi-send" @click:append="publicarComentarioEditado">
                    </v-text-field>
                    <v-text-field class="mt-5" label="Comentário" color="verdeClaro" :loading="carregando"
                        placeholder="Digite seu comentário." outlined v-model="novoComentario" v-else
                        :rules="[required('Comentário')]" append-icon="mdi-send" @click:append="criarComentario">
                    </v-text-field>
                </v-form>
            </v-container>
        </v-sheet>
    </v-bottom-sheet>

    <v-dialog v-model="dialogFuncao" max-width="290">
      <v-card color="fundoClaro">
        <v-card-title class="text-h5 verdeMedio--text">
          {{ textoDialog }}
        </v-card-title>
        <v-card-actions>
          <v-spacer></v-spacer>
          <v-btn color="verdeClaro" text
            @click="dialogFuncao = false; comentarioSelecionado = {};">
            Cancelar
          </v-btn>
          <v-btn color="verdeClaro" text v-if="funcaoDialog === 'REFUTAR'"
            @click="refutarComentario" :loading="carregando">
            Sim
          </v-btn>
          <v-btn color="verdeClaro" text v-else-if="funcaoDialog === 'DELETAR_PUBLICACAO'"
            @click="deletarPublicacao" :loading="carregando">
            Sim
          </v-btn>
          <v-btn color="verdeClaro" text v-else
            @click="deletarComentario" :loading="carregando">
            Sim
          </v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>

    <SnackbarNotificacao v-if="mostrarSnackbarNotificacao" :texto="mensagemSnackbar" :corSucesso="snackbarSucesso"
        @fechaSnackbar="mostrarSnackbarNotificacao = false"/>

    <LoginDialog v-if="mostrarLoginDialog" @fechaModal="mostrarLoginDialog = false" />

  </v-container>
</template>

<script>
import { mapGetters } from 'vuex'
import SnackbarNotificacao from "@/components/utils/SnackbarNotificacao.vue"
import LoginDialog from "@/components/LoginDialog.vue"

export default {
    name: 'VisualizarPublicacao',
    components:{
        SnackbarNotificacao,
        LoginDialog
    },
    data(){
        return{
            publicacaoRota: +this.$route.params.idPublicacao,
            publicacao: new Object(),
            carregando: false,
            mostrarSnackbarNotificacao: false,
            mensagemSnackbar: '',
            snackbarSucesso: true,
            carregandoPublicacao: true,
            mostrarLoginDialog: false,
            comentarioSheet: false,
            textoSheet: '',
            novoComentario: '',
            comentarioValido: false,
            comentarioSelecionado: new Object(),
            dialogFuncao: false,
            textoDialog: '',
            funcaoDialog: '',
            editando: false,
        }
    },
    methods:{
        async buscaPublicacao(){
            await this.$http.get("/publicacoes/" + this.publicacaoRota)
            .then((res)=>{
                this.carregandoPublicacao = false
                this.publicacao = res.data
            }).catch((err)=>{
                this.mostrarSnackbarNotificacao = true
                this.mensagemSnackbar = err.response.data.message
                this.snackbarSucesso = false
                setTimeout(()=>{
                    this.$router.push('/')
                }, 2000)
            })
        },

        adicionarComentario(){
            if(!this.usuario){
                this.mostrarLoginDialog = true
            }else{
                this.textoSheet = "Novo comentário: "
                this.comentarioSheet = true
            }
        },

        async criarComentario(){
            if(!this.novoComentario){
                this.snackbarSucesso = false
                this.mensagemSnackbar = "Comentário inválido!"
                this.mostrarSnackbarNotificacao = true
                return
            }
            this.carregando = true
            
            await this.$http.post('/comentarios',{
                conteudo: this.novoComentario,
                idPublicacao: this.publicacao.id,
                idUsuario: this.usuario.id
            }).then(()=>{
                this.mensagemSnackbar = 'Comentário publicado com sucesso!'
                this.snackbarSucesso = true
                this.mostrarSnackbarNotificacao = true
                setTimeout(()=>{
                    this.buscaPublicacao()
                    this.carregando = false
                }, 1000)
            }).catch((err)=>{
                this.mensagemSnackbar = err.response.data.message
                this.snackbarSucesso = false
                this.mostrarSnackbarNotificacao = true
                this.carregando = false
            })

            this.comentarioSheet = false
        },

        editarComentario(comentario){
            console.log(comentario)
            this.comentarioSelecionado = comentario
            this.textoSheet = "Editar comentário: "
            this.novoComentario = comentario.conteudo
            this.comentarioSheet = true
        },

        async publicarComentarioEditado(){
            this.carregando = true

            await this.$http.put('/comentarios/' + this.comentarioSelecionado.id,{
                conteudo: this.novoComentario
            }).then(()=>{
                this.mensagemSnackbar = 'Comentário editado com sucesso!'
                this.snackbarSucesso = true
                this.mostrarSnackbarNotificacao = true
                setTimeout(()=>{
                    this.buscaPublicacao()
                    this.carregando = false
                    this.comentarioSelecionado = {}
                }, 1000)
            }).catch((err)=>{
                this.mensagemSnackbar = err.response.data.message
                this.snackbarSucesso = false
                this.mostrarSnackbarNotificacao = true
                this.carregando = false
                this.comentarioSelecionado = {}
            })

            this.novoComentario = ''
        },

        selecionarComentario(comentario, funcao){
            if(!this.usuario){
                this.mostrarLoginDialog = true
                return
            }
            if(!this.usuario.farmaceutico && funcao === 'REFUTAR'){
                this.mensagemSnackbar = 'Você precisa ser um usuário farmacêutico para refutar um comentário!'
                this.snackbarSucesso = false
                this.mostrarSnackbarNotificacao = true
                this.comentarioSelecionado = {}
                return
            }

            this.comentarioSelecionado = comentario
            this.funcaoDialog = funcao
            if(comentario.refutado){
                this.textoDialog = "Cancelar refutação do comentário?"
            }else{
                this.textoDialog = funcao === 'REFUTAR' ? "Refutar comentário?" : "Excluir comentário?"
            }
            this.dialogFuncao = true
        },

        async deletarComentario(){
            this.carregando = true

            await this.$http.delete('/comentarios/' + this.comentarioSelecionado.id)
            .then(()=>{
                this.mensagemSnackbar = 'Comentário deletado com sucesso!'
                this.snackbarSucesso = true
                this.mostrarSnackbarNotificacao = true
                setTimeout(()=>{
                    this.buscaPublicacao()
                    this.carregando = false
                    this.dialogFuncao = false
                }, 1000)
            }).catch((err)=>{
                this.mensagemSnackbar = err.response.data.message
                this.snackbarSucesso = false
                this.mostrarSnackbarNotificacao = true
                this.carregando = false
                this.dialogFuncao = false
            })
            this.comentarioSelecionado = {}
        },

        async refutarComentario(){
            this.carregando = true

            await this.$http.put('/farmaceuticos/refutarComentario/' + this.comentarioSelecionado.id)
            .then(()=>{
                this.mensagemSnackbar = 'Comentário atualizado!'
                this.snackbarSucesso = true
                this.mostrarSnackbarNotificacao = true
                setTimeout(()=>{
                    this.buscaPublicacao()
                    this.carregando = false
                    this.dialogFuncao = false
                }, 1000)
            }).catch((err)=>{
                this.mensagemSnackbar = err.response.data.message
                this.snackbarSucesso = false
                this.mostrarSnackbarNotificacao = true
                this.carregando = false
                this.dialogFuncao = false
            })
            this.comentarioSelecionado = {}
        },

        async deletarPublicacao(){
            this.carregando = true
            await this.$http.delete('/publicacoes/' + this.publicacao.id)
            .then(()=>{
                this.carregandoPublicacao = true
                this.mensagemSnackbar = 'Publicação excluída!'
                this.snackbarSucesso = true
                this.mostrarSnackbarNotificacao = true
                setTimeout(()=>{
                    this.$router.push('/')
                }, 1000)
            }).catch((err)=>{
                this.mensagemSnackbar = err.response.data.message
                this.snackbarSucesso = false
                this.mostrarSnackbarNotificacao = true
            })
            this.dialogFuncao = false
            this.carregando = false
        },

        async editarPublicacao(){
            this.carregando = true

            await this.$http.put('/publicacoes/' + this.publicacao.id, {
                conteudo: this.publicacao.conteudo,
                titulo: this.publicacao.titulo
            }).then((res)=>{
                this.mensagemSnackbar = 'Publicação atualizada!'
                this.snackbarSucesso = true
                this.mostrarSnackbarNotificacao = true
                this.publicacao = res.data
            }).catch((err)=>{
                this.mensagemSnackbar = err.response.data.message
                this.snackbarSucesso = false
                this.mostrarSnackbarNotificacao = true
            })

            this.editando = false
            this.carregando = false
        },

        tamanhoMinimo(propriedade, tamanho){
            return v =>  v && v.length >= tamanho || `${propriedade} precisa de ${tamanho} caracteres.`
        },
        tamanhoMaximo(propriedade, tamanho){
            return v =>  v && v.length <= tamanho || `O número máximo de caracteres de ${propriedade} é ${tamanho}`
        },
        required(propriedade){
            return v =>  v && v.length > 0 || `${propriedade} é um campo necessário`
        },
    },
    computed:{
        ...mapGetters(['usuario'])
    },
    created(){
        this.buscaPublicacao()
    }
}
</script>

<style>

</style>