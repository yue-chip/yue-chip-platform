/*
 Navicat Premium Data Transfer

 Source Server         : 192.168.177.129
 Source Server Type    : MySQL
 Source Server Version : 80029
 Source Host           : 192.168.177.129:3306
 Source Schema         : oauth2

 Target Server Type    : MySQL
 Target Server Version : 80029
 File Encoding         : 65001

 Date: 24/05/2023 15:45:42
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for oauth2_authorization
-- ----------------------------
DROP TABLE IF EXISTS `oauth2_authorization`;
CREATE TABLE `oauth2_authorization` (
  `id` varchar(100) NOT NULL,
  `registered_client_id` varchar(100) NOT NULL,
  `principal_name` varchar(200) NOT NULL,
  `authorization_grant_type` varchar(100) NOT NULL,
  `authorized_scopes` varchar(1000) DEFAULT NULL,
  `attributes` blob,
  `state` varchar(500) DEFAULT NULL,
  `authorization_code_value` blob,
  `authorization_code_issued_at` timestamp NULL DEFAULT NULL,
  `authorization_code_expires_at` timestamp NULL DEFAULT NULL,
  `authorization_code_metadata` blob,
  `access_token_value` blob,
  `access_token_issued_at` timestamp NULL DEFAULT NULL,
  `access_token_expires_at` timestamp NULL DEFAULT NULL,
  `access_token_metadata` blob,
  `access_token_type` varchar(100) DEFAULT NULL,
  `access_token_scopes` varchar(1000) DEFAULT NULL,
  `oidc_id_token_value` blob,
  `oidc_id_token_issued_at` timestamp NULL DEFAULT NULL,
  `oidc_id_token_expires_at` timestamp NULL DEFAULT NULL,
  `oidc_id_token_metadata` blob,
  `refresh_token_value` blob,
  `refresh_token_issued_at` timestamp NULL DEFAULT NULL,
  `refresh_token_expires_at` timestamp NULL DEFAULT NULL,
  `refresh_token_metadata` blob,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Records of oauth2_authorization
-- ----------------------------
BEGIN;
INSERT INTO `oauth2_authorization` (`id`, `registered_client_id`, `principal_name`, `authorization_grant_type`, `authorized_scopes`, `attributes`, `state`, `authorization_code_value`, `authorization_code_issued_at`, `authorization_code_expires_at`, `authorization_code_metadata`, `access_token_value`, `access_token_issued_at`, `access_token_expires_at`, `access_token_metadata`, `access_token_type`, `access_token_scopes`, `oidc_id_token_value`, `oidc_id_token_issued_at`, `oidc_id_token_expires_at`, `oidc_id_token_metadata`, `refresh_token_value`, `refresh_token_issued_at`, `refresh_token_expires_at`, `refresh_token_metadata`) VALUES ('aa0affbc-e1d9-45d1-a222-59e9d8105b50', '0741eeb8-d5c3-4bff-8cfa-019a9ce32615', 'admin', 'password', 'test,update,write,delete', 0x7B2240636C617373223A226A6176612E7574696C2E436F6C6C656374696F6E7324556E6D6F6469666961626C654D6170227D, NULL, NULL, NULL, NULL, NULL, 0x65794A72615751694F694931597A67304E6A56695A533033595464694C54526C5932497459546C6B4F43316D5A54566A4D6A566C4D6D45324E444D694C434A68624763694F694A53557A49314E694A392E65794A7A645749694F694A3564575574593268706343316A62476C6C626E51694C434A68645751694F694A3564575574593268706343316A62476C6C626E51694C434A75596D59694F6A45324E7A63784D6A45344E444D73496E4E6A6233426C496A7062496E526C633351694C434A3163475268644755694C434A33636D6C305A534973496D526C624756305A534A644C434A7063334D694F694A6F644852774F6938764D5463794C6A45324C6A49304D4334784F6A4D334D7A5178496977695A586877496A6F784E6A63334D54497A4E6A517A4C434A70595851694F6A45324E7A63784D6A45344E444E392E4175634856527363784F6F334D427463736E5A583437433647454F34324669754F4A706F5A76764D5174426E6D463243506B6D5456636B4E39674579354331757830396B53426144593258434859756F5A625337375844415743565272674868786850413245634C4A61506B7A624B425144544D706D6C315A486669594E706D436633644E65677368775F2D505A71666755476A4E54644966455474734962695F63733433484B794F6A446334466C704A614145694B625131456E6557477536435570514E547134646F4C436C56675445434230503564767953417041303950345656666A585142523367494F7169445F3131334E595554656F71745179574856566F54355F354C727242365846543644676373526E6649592D5F6655724B657543436967344B387662505A4C6F4D6E6B6D4D616B48436E71412D4438723649665F366C53793634784561426A79375247376D514651, '2023-02-23 03:10:44', '2023-02-23 03:40:44', 0x7B2240636C617373223A226A6176612E7574696C2E436F6C6C656374696F6E7324556E6D6F6469666961626C654D6170222C226D657461646174612E746F6B656E2E696E76616C696461746564223A66616C73657D, 'Bearer', 'test,update,write,delete', NULL, NULL, NULL, NULL, 0x3130384657385255725932536A534A4E4C707A57717331746F4E76744D4E484E70625A47595A43477755625452456737582D71536650644D3432327A4243643567304A52753148565635755F2D5748636E3277664D4A39707A752D71564F696C50756844335F6F6F554A636635796C52632D3075394D4F70597748394E304659, '2023-02-23 03:10:44', '2023-02-24 03:10:44', 0x7B2240636C617373223A226A6176612E7574696C2E436F6C6C656374696F6E7324556E6D6F6469666961626C654D6170222C226D657461646174612E746F6B656E2E696E76616C696461746564223A66616C73657D);
INSERT INTO `oauth2_authorization` (`id`, `registered_client_id`, `principal_name`, `authorization_grant_type`, `authorized_scopes`, `attributes`, `state`, `authorization_code_value`, `authorization_code_issued_at`, `authorization_code_expires_at`, `authorization_code_metadata`, `access_token_value`, `access_token_issued_at`, `access_token_expires_at`, `access_token_metadata`, `access_token_type`, `access_token_scopes`, `oidc_id_token_value`, `oidc_id_token_issued_at`, `oidc_id_token_expires_at`, `oidc_id_token_metadata`, `refresh_token_value`, `refresh_token_issued_at`, `refresh_token_expires_at`, `refresh_token_metadata`) VALUES ('d5cd3e82-3e2e-4d02-b73c-2ca57b23669e', '0741eeb8-d5c3-4bff-8cfa-019a9ce32615', 'admin', 'password', 'test,update,write,delete', 0x7B2240636C617373223A226A6176612E7574696C2E436F6C6C656374696F6E7324556E6D6F6469666961626C654D6170227D, NULL, NULL, NULL, NULL, NULL, 0x65794A72615751694F694931597A67304E6A56695A533033595464694C54526C5932497459546C6B4F43316D5A54566A4D6A566C4D6D45324E444D694C434A68624763694F694A53557A49314E694A392E65794A7A645749694F694A3564575574593268706343316A62476C6C626E51694C434A68645751694F694A3564575574593268706343316A62476C6C626E51694C434A75596D59694F6A45324E7A63784D6A45354D6A4173496E4E6A6233426C496A7062496E526C633351694C434A3163475268644755694C434A33636D6C305A534973496D526C624756305A534A644C434A7063334D694F694A6F644852774F6938764D5463794C6A45324C6A49304D4334784F6A4D334D7A5178496977695A586877496A6F784E6A63334D54497A4E7A49774C434A70595851694F6A45324E7A63784D6A45354D6A42392E4E4142517835457239432D365748656A4F367534504D7350396E797746484C68795567304E4135466F31576E51465643643757696B34784B6C784C512D6C32344E6B73394E393074797934337472454C6757796749724433707450646C554F5F35716C4279654D47795365666B336B5F31656E5556555350346974684D32737958775A58745852514244396E4F7749487A676370335253424636754342636176394E68614759666B69346F6469695A30507A59704B4D7046664A6E682D65387631655057452D4C67414B4E544836504951505479366E77576F744A7747663650435F715369316D4176497A6276694D674E7242484671423154456F57526B3468507044334938596C7258494B797672795932397A4F48656D38744D716A6D522D737761466B467270706363634E645F6B62533152354D7968374769696F2D5555746E6958386C4A4668446461635061697464356E6451, '2023-02-23 03:12:00', '2023-02-23 03:42:00', 0x7B2240636C617373223A226A6176612E7574696C2E436F6C6C656374696F6E7324556E6D6F6469666961626C654D6170222C226D657461646174612E746F6B656E2E696E76616C696461746564223A66616C73657D, 'Bearer', 'test,update,write,delete', NULL, NULL, NULL, NULL, 0x754D664431344A63456F46326A426A7A3767452D6868637931726568386C467351425A4C376D507762686E525432546A62326F72745F5847307254506149397371635A654231396D2D7674374E33435735743672586C62727443352D53417065457670475146435767713371706E465634577879507359537444776D4A725768, '2023-02-23 03:12:00', '2023-02-24 03:12:00', 0x7B2240636C617373223A226A6176612E7574696C2E436F6C6C656374696F6E7324556E6D6F6469666961626C654D6170222C226D657461646174612E746F6B656E2E696E76616C696461746564223A66616C73657D);
INSERT INTO `oauth2_authorization` (`id`, `registered_client_id`, `principal_name`, `authorization_grant_type`, `authorized_scopes`, `attributes`, `state`, `authorization_code_value`, `authorization_code_issued_at`, `authorization_code_expires_at`, `authorization_code_metadata`, `access_token_value`, `access_token_issued_at`, `access_token_expires_at`, `access_token_metadata`, `access_token_type`, `access_token_scopes`, `oidc_id_token_value`, `oidc_id_token_issued_at`, `oidc_id_token_expires_at`, `oidc_id_token_metadata`, `refresh_token_value`, `refresh_token_issued_at`, `refresh_token_expires_at`, `refresh_token_metadata`) VALUES ('e8cf3c01-b8a1-4859-aead-9b694ffc243f', '0741eeb8-d5c3-4bff-8cfa-019a9ce32615', 'admin', 'password', 'test,update,write,delete', 0x7B2240636C617373223A226A6176612E7574696C2E436F6C6C656374696F6E7324556E6D6F6469666961626C654D6170227D, NULL, NULL, NULL, NULL, NULL, 0x65794A72615751694F694931597A67304E6A56695A533033595464694C54526C5932497459546C6B4F43316D5A54566A4D6A566C4D6D45324E444D694C434A68624763694F694A53557A49314E694A392E65794A7A645749694F694A3564575574593268706343316A62476C6C626E51694C434A68645751694F694A3564575574593268706343316A62476C6C626E51694C434A75596D59694F6A45324E7A63784D6A45344E446373496E4E6A6233426C496A7062496E526C633351694C434A3163475268644755694C434A33636D6C305A534973496D526C624756305A534A644C434A7063334D694F694A6F644852774F6938764D5463794C6A45324C6A49304D4334784F6A4D334D7A5178496977695A586877496A6F784E6A63334D54497A4E6A51334C434A70595851694F6A45324E7A63784D6A45344E4464392E4C6D4767306B7A71574634326D716273335651584359323275666F684D563163514B2D49427A59456F6877336254464957426D714E725455736F4E6378337A717A61704147376F7256557365555F526F427650505A64544954794F383163687A45477A68544654475675644B6857304A30686979374A62434B48524437695245576B78515F4F585171456A704C57324C6E662D553450386230595F5F4C6569684E7153424E6C6B4A6261745A3241737653434F435139474D5A584B737341725235436C57576B445A37617377624C3775654C4E785F4D4B4B372D4763527841376E723232715076764E776B50713977744E35756B4F727156714C6B46576A564745595A5551776C743759507835346D4A6877523867755F5A7565685F354570323739437151393062366B587364344F4C4C56317A4A617178387A635A55794F486F5F72414E5638493645724C626732766C6638327067, '2023-02-23 03:10:48', '2023-02-23 03:40:48', 0x7B2240636C617373223A226A6176612E7574696C2E436F6C6C656374696F6E7324556E6D6F6469666961626C654D6170222C226D657461646174612E746F6B656E2E696E76616C696461746564223A66616C73657D, 'Bearer', 'test,update,write,delete', NULL, NULL, NULL, NULL, 0x6A746E75314C6E687A6461764766723254795F6A567842694C46716A3861305950476B6D79647668396B4C3367306B626A5930353254635552793369793349343435666E6C5336454C6D48676961397A3959327454495933455344532D4165573832374F61636E2D4C722D432D454D6671677467674A2D47417A55366D585A48, '2023-02-23 03:10:48', '2023-02-24 03:10:48', 0x7B2240636C617373223A226A6176612E7574696C2E436F6C6C656374696F6E7324556E6D6F6469666961626C654D6170222C226D657461646174612E746F6B656E2E696E76616C696461746564223A66616C73657D);
INSERT INTO `oauth2_authorization` (`id`, `registered_client_id`, `principal_name`, `authorization_grant_type`, `authorized_scopes`, `attributes`, `state`, `authorization_code_value`, `authorization_code_issued_at`, `authorization_code_expires_at`, `authorization_code_metadata`, `access_token_value`, `access_token_issued_at`, `access_token_expires_at`, `access_token_metadata`, `access_token_type`, `access_token_scopes`, `oidc_id_token_value`, `oidc_id_token_issued_at`, `oidc_id_token_expires_at`, `oidc_id_token_metadata`, `refresh_token_value`, `refresh_token_issued_at`, `refresh_token_expires_at`, `refresh_token_metadata`) VALUES ('ff545ba4-46d3-4d8d-be5a-d9fde44718f0', '0741eeb8-d5c3-4bff-8cfa-019a9ce32615', 'admin', 'password', 'test,update,write,delete', 0x7B2240636C617373223A226A6176612E7574696C2E436F6C6C656374696F6E7324556E6D6F6469666961626C654D6170227D, NULL, NULL, NULL, NULL, NULL, 0x65794A72615751694F694931597A67304E6A56695A533033595464694C54526C5932497459546C6B4F43316D5A54566A4D6A566C4D6D45324E444D694C434A68624763694F694A53557A49314E694A392E65794A7A645749694F694A3564575574593268706343316A62476C6C626E51694C434A68645751694F694A3564575574593268706343316A62476C6C626E51694C434A75596D59694F6A45324E7A63784D6A45344E446773496E4E6A6233426C496A7062496E526C633351694C434A3163475268644755694C434A33636D6C305A534973496D526C624756305A534A644C434A7063334D694F694A6F644852774F6938764D5463794C6A45324C6A49304D4334784F6A4D334D7A5178496977695A586877496A6F784E6A63334D54497A4E6A51344C434A70595851694F6A45324E7A63784D6A45344E4468392E6E695F307649425F61343646436634464358686B2D4D71317161555873435271695F57556844464D524A5F6D58706C31526942717A4C4430635F32694F65722D56615637395374524151636A746A556238614756424A5769385255645A6D58515F356137337132706A36737534674343546D467A425958696C2D7169356B653758474A4D616A506B714A55756D59385163365A2D353057534A6E5270556F68366532367555357142316C4B6D734E47496669655568594858534539796D584B463956365A587A6568396C414F3242787737346F7037476F444B387178317256314352336E664E6F473434536A5542335853474C767A775565434351755339654F4749764E48493338533276585356737A794D525A68306265586147647A64544C594977525755485F6333454D666C37566A6F4632314A4B6A3158414A57495059337439463335486A6265364958474E42345559706241, '2023-02-23 03:10:49', '2023-02-23 03:40:49', 0x7B2240636C617373223A226A6176612E7574696C2E436F6C6C656374696F6E7324556E6D6F6469666961626C654D6170222C226D657461646174612E746F6B656E2E696E76616C696461746564223A66616C73657D, 'Bearer', 'test,update,write,delete', NULL, NULL, NULL, NULL, 0x3136774A58417A7343444630736C766D56766163496C5077613373565A566F63627A656D6135666A6A484F54394A444361626D5A474A594E786675776D4D636A5F494551666F457A4D67774F54326B6F666843705A505F6E4439427168323254417A6763546F344E386E757A716B447568417151494C5A32416753546D675F63, '2023-02-23 03:10:49', '2023-02-24 03:10:49', 0x7B2240636C617373223A226A6176612E7574696C2E436F6C6C656374696F6E7324556E6D6F6469666961626C654D6170222C226D657461646174612E746F6B656E2E696E76616C696461746564223A66616C73657D);
COMMIT;

-- ----------------------------
-- Table structure for oauth2_authorization_consent
-- ----------------------------
DROP TABLE IF EXISTS `oauth2_authorization_consent`;
CREATE TABLE `oauth2_authorization_consent` (
  `registered_client_id` varchar(100) NOT NULL,
  `principal_name` varchar(200) NOT NULL,
  `authorities` varchar(1000) NOT NULL,
  PRIMARY KEY (`registered_client_id`,`principal_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Records of oauth2_authorization_consent
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for oauth2_registered_client
-- ----------------------------
DROP TABLE IF EXISTS `oauth2_registered_client`;
CREATE TABLE `oauth2_registered_client` (
  `id` varchar(100) NOT NULL,
  `client_id` varchar(100) NOT NULL,
  `client_id_issued_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `client_secret` varchar(200) DEFAULT NULL,
  `client_secret_expires_at` timestamp NULL DEFAULT NULL,
  `client_name` varchar(200) NOT NULL,
  `client_authentication_methods` varchar(1000) NOT NULL,
  `authorization_grant_types` varchar(1000) NOT NULL,
  `redirect_uris` varchar(1000) DEFAULT NULL,
  `scopes` varchar(1000) NOT NULL,
  `client_settings` varchar(2000) NOT NULL,
  `token_settings` varchar(2000) NOT NULL,
  `post_logout_redirect_uris` varchar(2000) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Records of oauth2_registered_client
-- ----------------------------
BEGIN;
INSERT INTO `oauth2_registered_client` (`id`, `client_id`, `client_id_issued_at`, `client_secret`, `client_secret_expires_at`, `client_name`, `client_authentication_methods`, `authorization_grant_types`, `redirect_uris`, `scopes`, `client_settings`, `token_settings`, `post_logout_redirect_uris`) VALUES ('0741eeb8-d5c3-4bff-8cfa-019a9ce32615', 'yue-chip-client', '2023-01-02 01:38:45', '{bcrypt}$2a$10$KKuAnpSY.n.WDXAq4YCp4.jSEC9IQkpDZTKIXYOuTUnPFap4ynUxW', '2023-12-31 00:00:00', 'yue-chip', 'client_secret_post,client_secret_jwt,client_secret_basic', 'refresh_token,client_credentials,password,authorization_code', 'http://www.baidu.com/callback', 'test,update,write,delete', '{\"@class\":\"java.util.Collections$UnmodifiableMap\",\"settings.client.require-proof-key\":false,\"settings.client.require-authorization-consent\":true}', '{\"@class\":\"java.util.Collections$UnmodifiableMap\",\"settings.token.reuse-refresh-tokens\":true,\"settings.token.id-token-signature-algorithm\":[\"org.springframework.security.oauth2.jose.jws.SignatureAlgorithm\",\"RS256\"],\"settings.token.access-token-time-to-live\":[\"java.time.Duration\",1800.000000000],\"settings.token.access-token-format\":{\"@class\":\"org.springframework.security.oauth2.server.authorization.settings.OAuth2TokenFormat\",\"value\":\"self-contained\"},\"settings.token.refresh-token-time-to-live\":[\"java.time.Duration\",86400.000000000],\"settings.token.authorization-code-time-to-live\":[\"java.time.Duration\",600.000000000]}', '');
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;
