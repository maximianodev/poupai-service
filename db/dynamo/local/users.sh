aws dynamodb create-table \
    --table-name users \
    --attribute-definitions \
        AttributeName=id,AttributeType=S \
        AttributeName=email,AttributeType=S \
    --key-schema \
        AttributeName=id,KeyType=HASH \
    --global-secondary-indexes \
        "[
          {
            \"IndexName\": \"email-index\",
            \"KeySchema\": [
              {\"AttributeName\":\"email\",\"KeyType\":\"HASH\"}
            ],
            \"Projection\":{
              \"ProjectionType\":\"ALL\"
            }
          }
        ]" \
    --billing-mode PAY_PER_REQUEST \
    --table-class STANDARD \
    --region us-east-2 \
    $(if [ "$(read -p "Ambiente (prod/local): " env && echo $env)" = "prod" ]; then echo ""; else echo "--endpoint-url http://localhost:8000"; fi) \
    --profile $(read -p "aws profile: " profile && echo $profile)