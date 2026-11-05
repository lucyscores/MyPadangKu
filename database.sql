/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Other/SQLTemplate.sql to edit this template
 */
/**
 * Author:  MSI_
 * Created: Nov 5, 2026
 */

CREATE DATABASE IF NOT EXISTS MyPadangKu;
USE MyPadangKu;

CREATE TABLE IF NOT EXISTS menu (
    id_menu INT AUTO_INCREMENT PRIMARY KEY,
    nama_menu VARCHAR(100) NOT NULL,
    harga_menu INT NOT NULL
);

CREATE TABLE IF NOT EXISTS resep (
    id_resep INT AUTO_INCREMENT PRIMARY KEY,
    id_menu INT NOT NULL,
    nama_resep VARCHAR(100) NOT NULL,
    deskripsi TEXT,
    UNIQUE (id_menu),
    CONSTRAINT fk_resep_menu FOREIGN KEY(id_menu)
        REFERENCES menu(id_menu)
)

CREATE TABLE IF NOT EXISTS detail_resep (
    id_detail INT AUTO_INCREMENT PRIMARY KEY,
    id_resep INT NOT NULL,
    nama_bahan VARCHAR(100) NOT NULL,
    jumlah DECIMAL(10,2) NOT NULL,
    satuan VARCHAR(20) NOT NULL,
    CONSTRAINT fk_detail_resep FOREIGN KEY (id_resep)
        REFERENCES resep(id_resep) ON DELETE CASCADE
)

INSERT INTO menu (nama_menu, harga_menu)
SELECT 'Rendang', 25000
WHERE NOT EXISTS (SELECT 1 FROM menu WHERE nama_menu = 'Rendang');

INSERT INTO menu (nama_menu, harga_menu)
SELECT 'Ayam Pop', 22000
WHERE NOT EXISTS (SELECT 1 FROM menu WHERE nama_menu = 'Ayam Pop')