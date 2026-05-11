package es.cifpp.gestion_inventario_cifpp.repositorios;

import org.springframework.data.mongodb.repository.MongoRepository;

import es.cifpp.gestion_inventario_cifpp.entidades.Estado;

public interface RepositorioEstado extends MongoRepository<Estado, String/*Tipo de id*/>{
   //Con Spring boot ya viene por defecto el CRUD. pero es necesario que el repositorio exista y indicarle que objeto y que tipo de
   // id necesita 
}