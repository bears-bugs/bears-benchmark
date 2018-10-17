; Management Server Bootstrap. Endpoint given as first argument and layout as JSON file
(in-ns 'org.corfudb.shell)                                  ; so our IDE knows what NS we are using

(import org.docopt.Docopt)                                  ; parse some cmdline opts
(import org.corfudb.runtime.view.Layout)

(def usage "corfu_management_bootstrap, to bootstrap Corfu Management Server.
Usage:
  corfu_management_bootstrap -c <config> -l <layout> [-e [-u <keystore> -f <keystore_password_file>] [-r <truststore> -w <truststore_password_file>] [-g -o <username_file> -j <password_file>]]
Options:
  -l <layout>, --layout <layout>                                                         Layout.
  -c <config>, --config <config>                                                         Configuration string to use.
  -e, --enable-tls                                                                       Enable TLS.
  -u <keystore>, --keystore=<keystore>                                                   Path to the key store.
  -f <keystore_password_file>, --keystore-password-file=<keystore_password_file>         Path to the file containing the key store password.
  -r <truststore>, --truststore=<truststore>                                             Path to the trust store.
  -w <truststore_password_file>, --truststore-password-file=<truststore_password_file>   Path to the file containing the trust store password.
  -g, --enable-sasl-plain-text-auth                                                      Enable SASL Plain Text Authentication.
  -o <username_file>, --sasl-plain-text-username-file=<username_file>                    Path to the file containing the username for SASL Plain Text Authentication.
  -j <password_file>, --sasl-plain-text-password-file=<password_file>                    Path to the file containing the password for SASL Plain Text Authentication.
  -h, --help     Show this screen.
")

(def localcmd (.. (new Docopt usage) (parse *args)))

(defn build-layout [endpoint layout] (do
                                       (get-router endpoint localcmd)
                                       (let [q (.. (get-management-client) (bootstrapManagement (Layout/fromJSONString (str layout))))]
                                            (.. q (get))
                                            )
                                       (println endpoint "bootstrapped successfully")
                                       ))

; a function which builds and bootstraps the layout
(defn bootstrap-node [endpoint, layout] (do
                                          (build-layout endpoint (str (slurp layout)))
                                          ))

(bootstrap-node (.. localcmd (get "--config")) (.. localcmd (get "--layout")))
