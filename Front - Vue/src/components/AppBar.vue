<template>
  <nav>
    <v-app-bar
        class="verdeClaro"
        dark elevation="0">
        <v-hover>
            <v-toolbar-title @click="$router.push('/')" style="cursor: pointer;"><img src="@/assets/logoBege.svg" alt="Logo"></v-toolbar-title>
        </v-hover>
        <v-spacer></v-spacer>
        <v-tooltip bottom>
            
            <template v-slot:activator="{ on, attrs }">
                <v-btn @click="abreLoginDialog" color="verdeMedio" fab small elevation="0" v-if="!usuario"
                    v-bind="attrs" v-on="on">
                    <v-icon color="fundo">mdi-account</v-icon>
                </v-btn>
                <v-btn @click="abrePerfilDialog" color="verdeMedio" small elevation="0" v-else
                    v-bind="attrs" v-on="on">
                    <v-icon color="fundo" left>mdi-pencil</v-icon>
                     /    
                    <v-icon color="fundo" right>mdi-logout</v-icon>
                </v-btn>
            </template>
            <span v-if="!usuario">Entrar / Registrar-se</span>
            <span v-else>Editar Perfil / Sair</span>
        </v-tooltip>
    </v-app-bar>
    <LoginDialog v-if="loginDialog"
        @fechaModal="loginDialog = false"
    />
    <PerfilDialog v-if="perfilDialog" :usuario="usuario"
        @fechaModal="perfilDialog = false"
    />
  </nav>
</template>

<script>
import LoginDialog from './LoginDialog.vue'
import PerfilDialog from './PerfilDialog.vue'
import { mapGetters } from 'vuex'

export default{
    name: "NavBar",
    data(){
        return{
            loginDialog: false,
            perfilDialog: false
        }
    },
    methods: {
        abreLoginDialog(){
            this.loginDialog = true
        },
        abrePerfilDialog(){
            this.perfilDialog = true
        }
    },
    components:{
        LoginDialog,
        PerfilDialog
    },
    computed:{
        ...mapGetters(['usuario'])
    }
    
}
</script>

<style scoped>

</style>