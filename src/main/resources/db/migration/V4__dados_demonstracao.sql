-- Dados de demonstração do AlertaSP.
-- As coordenadas são aproximadas e servem apenas para a demonstração; não representam
-- pontos oficiais de alagamento nem locais recomendados para instalação de sensores.

insert into ponto_monitorado (nome, descricao, bairro, latitude, longitude, nivel_risco_atual, atualizado_em) values
    ('Ponte das Bandeiras', 'Ponto de demonstração - Marginal Tietê', 'Santana', -23.5175, -46.6290, 'BAIXO', current_timestamp),
    ('Ponte do Limão', 'Ponto de demonstração - Marginal Tietê', 'Limão', -23.5135, -46.6710, 'BAIXO', current_timestamp),
    ('Ponte da Vila Guilherme', 'Ponto de demonstração - Marginal Tietê', 'Vila Guilherme', -23.5160, -46.6050, 'BAIXO', current_timestamp),
    ('Ponte Cidade Jardim', 'Ponto de demonstração - Marginal Pinheiros', 'Itaim Bibi', -23.5850, -46.6930, 'BAIXO', current_timestamp),
    ('Av. Aricanduva x Radial Leste', 'Ponto de demonstração - Zona Leste', 'Tatuapé', -23.5440, -46.5560, 'BAIXO', current_timestamp);

insert into sensor (codigo, ponto_id, altura_instalacao_cm, limite_alerta_cm, status)
select 'ESP32-001', id, 200, 30, 'ATIVO' from ponto_monitorado where nome = 'Ponte das Bandeiras';
insert into sensor (codigo, ponto_id, altura_instalacao_cm, limite_alerta_cm, status)
select 'ESP32-002', id, 200, 30, 'ATIVO' from ponto_monitorado where nome = 'Ponte do Limão';
insert into sensor (codigo, ponto_id, altura_instalacao_cm, limite_alerta_cm, status)
select 'ESP32-003', id, 200, 30, 'ATIVO' from ponto_monitorado where nome = 'Ponte da Vila Guilherme';
insert into sensor (codigo, ponto_id, altura_instalacao_cm, limite_alerta_cm, status)
select 'ESP32-004', id, 200, 30, 'ATIVO' from ponto_monitorado where nome = 'Ponte Cidade Jardim';
insert into sensor (codigo, ponto_id, altura_instalacao_cm, limite_alerta_cm, status)
select 'ESP32-005', id, 200, 30, 'ATIVO' from ponto_monitorado where nome = 'Av. Aricanduva x Radial Leste';

insert into usuario (nome, email) values ('Cidadã Demo', 'demo@alertasp.com');

insert into rota (usuario_id, nome, horario_saida, raio_alerta_metros)
select id, 'Casa → Trabalho', '07:30:00', 300 from usuario where email = 'demo@alertasp.com';

-- Trajeto Tatuapé → Av. Paulista (passa a menos de 300 m do ponto Av. Aricanduva x Radial Leste)
insert into rota_coordenada (rota_id, ordem, latitude, longitude)
select r.id, c.ordem, c.latitude, c.longitude
from rota r
join usuario u on u.id = r.usuario_id
cross join (values (0, -23.5450, -46.5540),
                   (1, -23.5405, -46.5760),
                   (2, -23.5480, -46.6150),
                   (3, -23.5530, -46.6380),
                   (4, -23.5614, -46.6559)) as c (ordem, latitude, longitude)
where u.email = 'demo@alertasp.com' and r.nome = 'Casa → Trabalho';

insert into local_favorito (usuario_id, nome, latitude, longitude, raio_alerta_metros)
select id, 'Casa', -23.5405, -46.5760, 300 from usuario where email = 'demo@alertasp.com';
