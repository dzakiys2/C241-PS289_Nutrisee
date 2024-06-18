const options = {
    definition: {
      openapi: "3.0.0",
      info: {
        title: "Nutrisee API",
        version: "1.0.0",
        description: "This is a simple API application made with Express and documented with Swagger"
      },
      servers: [
        {
          url: "http://localhost:3000",
          description: 'Development server'
        }
      ],
      components: {
        securitySchemes: {
          BearerAuth: {
            type: 'apiKey',
            in: 'header',
            name: 'Authorization',
            description: 'Enter your Firebase ID token prefixed with "Bearer ", get your token from using /get-token endpoint',
          },
        },
        schemas: {
          Product: {
            type: 'object',
            properties: {
              id: { type: 'string', description: 'The product ID' },
              merek: { type: 'string', description: 'The brand of the product' },
              nama_produk: { type: 'string', description: 'The name of the product' },
              barcode: { type: 'string', description: 'The barcode of the product' },
              barcode_url: { type: 'string', description: 'The URL of the barcode image' },
              isHalal: { type: 'boolean', description: 'Indicates if the product is Halal' },
              halal_description: { type: 'string', description: 'The Halal certification description' },
              nutrient_profiling_class: { type: 'string', description: 'The nutrient profiling class of the product' },
              nutriscore: { type: 'string', description: 'The URL of the Nutri-Score image' },
              nutriscore_label_description: { type: 'string', description: 'The description of the Nutri-Score' },
              nutrisi: {
                type: 'object',
                properties: {
                  energi: { type: 'string', description: 'The energy content' },
                  karbohidrat: {
                    type: 'object',
                    properties: {
                      gula: { type: 'string', description: 'The sugar content' },
                      total: { type: 'string', description: 'The total carbohydrate content' },
                      fiber: { type: 'string', description: 'The fiber content' }
                    }
                  },
                  lemak: {
                    type: 'object',
                    properties: {
                      jenuh: { type: 'string', description: 'The saturated fat content' },
                      total: { type: 'string', description: 'The total fat content' },
                      trans: { type: 'string', description: 'The trans fat content' }
                    }
                  },
                  protein: { type: 'string', description: 'The protein content' }
                }
              }
            }
          },
          UploadResponse: {
            type: 'object',
            properties: {
              data: {
                type: 'object',
                properties: {
                  merek: { type: 'string' },
                  nama_produk: { type: 'string' },
                  ukuran_porsi: { type: 'string' },
                  nutriscore: { type: 'string' },
                  nutrisi: {
                    type: 'object',
                    properties: {
                      energi: { type: 'string' },
                      karbohidrat: {
                        type: 'object',
                        properties: {
                          gula: { type: 'string' },
                          total: { type: 'string' }
                        }
                      },
                      lemak: {
                        type: 'object',
                        properties: {
                          jenuh: { type: 'string' },
                          total: { type: 'string' },
                          trans: { type: 'string' }
                        }
                      },
                      protein: { type: 'string' }
                    }
                  },
                  isHalal: { type: 'boolean' },
                  halal_description: { type: 'string' }
                }
              }
            }
          }
        },
        responses: {
          400: {
            description: 'Bad request, image not found',
            content: {
              'application/json': {}
            }
          },
          401: {
            description: 'Unauthorized, incorrect API key or incorrect format',
            content: {
              'application/json': {}
            }
          },
          404: {
            description: 'Not found, the product was not found',
            content: {
              'application/json': {}
            }
          },
          500: {
            description: 'Internal server error',
            content: {
              'application/json': {}
            }
          }
        }
      },
      security: [
        {
          BearerAuth: [],
        }
      ]
    },
    apis: ["./src/server.js", "./src/routes/*.js"] // Adjust the paths to your actual files
  };
  
  module.exports = { options };