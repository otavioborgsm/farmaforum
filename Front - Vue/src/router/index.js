import Vue from 'vue'
import VueRouter from 'vue-router'
import { getUsuario } from '@/services/UsuarioService.js'
import TelaInicio from '../views/TelaInicio.vue'
import NovoUsuario from '../views/NovoUsuario.vue'
import VisualizarPublicacao from '../views/VisualizarPublicacao.vue'
import NovaPublicacao from '../views/NovaPublicacao.vue'
import Pesquisa from '../views/TelaPesquisa.vue'

Vue.use(VueRouter)

const routes = [
  { path: '/', name: 'Inicio', component: TelaInicio },
  { path: '/criarConta', name: 'Criar Conta', component: NovoUsuario },
  { path: '/publicacao/:idPublicacao', name: 'Publicação', component: VisualizarPublicacao },
  { path: '/pesquisa', name: 'Pesquisa', component: Pesquisa },
  { path: '/novaPublicacao', name: 'Nova Publicação', component: NovaPublicacao, meta: { needsAuth: true } }
]

const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes
})

router.beforeEach((to, from, next) =>{
  if(to.meta.needsAuth){
    if(getUsuario()){
      next()
    }else{
      next('/')
    }
  }else{
    next()
  }
})

export default router
