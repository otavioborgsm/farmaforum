<template>
  <div>
    <v-container>
        <v-card color="fundoClaro" class="verdeEscuro--text">
            <v-card-title>
                <TextFieldPesquisa @clickEnter="pesquisar" @clickPesquisar="pesquisar" v-model="pesquisa"/>
            </v-card-title>
            <v-divider class="mb-0 pb-0"></v-divider>

            <v-card-text class="verdeEscuro--text">
                <center v-if="!pesquisa">
                    <h6 class="my-10 text-h6">Faça uma pesquisa para encontrar resultados.</h6>
                </center>
                <div v-else class="mt-0 pt-0">
                    <center v-if="listaPublicacoes === []">
                        <h6 class="my-10 text-h6">Sua pesquisa retornou vazia.</h6>
                    </center>
                    <template v-else v-for="(publicacao, index) in listaPublicacoes" >
                        <v-list-item :key="publicacao.id" link :to="'/publicacao/' +publicacao.id">
                            <template>
                                <v-list-item-content>
                                    <v-list-item-title v-text="publicacao.titulo" class="mt-2"></v-list-item-title>

                                    <v-list-item-subtitle class="text--primary my-3" >
                                        <v-chip v-for="farmaco in publicacao.farmacos" :key="'farmaco'+farmaco.id" small 
                                            color="verdeMedio" class="fundoClaro--text mr-1">
                                            {{farmaco.farmaco.descricao}}
                                        </v-chip>
                                    </v-list-item-subtitle>

                                    <v-list-item-subtitle class="mb-2" v-text="publicacao.usuario.nome + ' ' + publicacao.usuario.sobrenome"></v-list-item-subtitle>
                                </v-list-item-content>

                                
                            </template>
                        </v-list-item>

                        <v-divider
                            v-if="index < listaPublicacoes.length - 1"
                            :key="'i'+index"
                        ></v-divider>

                    </template>
                </div>
            </v-card-text>
        </v-card>
    </v-container>

    <SnackbarNotificacao v-if="mostrarSnackbarNotificacao" :texto="mensagemSnackbar" :corSucesso="snackbarSucesso"
        @fechaSnackbar="mostrarSnackbarNotificacao = false"/>
  </div>
</template>

<script>
import SnackbarNotificacao from "@/components/utils/SnackbarNotificacao.vue"
import TextFieldPesquisa from '@/components/utils/TextFieldPesquisa.vue'

export default {
    name: 'TelaPesquisa',
    components:{
        SnackbarNotificacao,
        TextFieldPesquisa
    },
    data(){
        return{
            pesquisa: '',
            mostrarSnackbarNotificacao: false,
            mensagemSnackbar: '',
            snackbarSucesso: true,
            listaPublicacoes: [],
            carregando: false
        }
    },
    methods:{
        async pesquisar(){
            if(!this.pesquisa){
                this.pesquisa = this.pesquisa = localStorage.getItem('pesquisa')
            }

            if(!localStorage.getItem('pesquisa') || localStorage.getItem('pesquisa') !== this.pesquisa){
                localStorage.setItem('pesquisa', this.pesquisa)
            }

            if(!this.pesquisa){
                this.mensagemSnackbar = 'Sua Pesquisa está vazia!'
                this.snackbarSucesso = false
                this.mostrarSnackbarNotificacao = true
                return
            }
            
            const setPesquisa = new Set()

            this.carregando = true
            await this.$http.post('/publicacoes/pesquisa', {
                pesquisa: this.pesquisa
            }).then((res)=>{
                let publicacoes = res.data

                if(publicacoes.length !== 0){
                    this.listaPublicacoes = publicacoes.filter((publicacao)=>{
                        const publicacaoDuplicada = setPesquisa.has(publicacao.id)
                        setPesquisa.add(publicacao.id)
                        return !publicacaoDuplicada
                    })
                }else{
                    this.listaPublicacoes = []
                }
            }).catch((err)=>{
                this.mostrarSnackbarNotificacao = true
                this.mensagemSnackbar = err.response.data.message
                this.snackbarSucesso = false
            })
            this.carregando = false
        }
    },
    created(){
        this.pesquisar()
    }

}
</script>

<style>

</style>