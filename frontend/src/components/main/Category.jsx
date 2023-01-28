import {
  Card,
  CardActionArea,
  CardMedia,
  Stack,
  Typography,
} from '@mui/material';
import React from 'react';

const Category = props => {
  return (
    <Stack alignItems="center" width="90px">
      <Card sx={{maxWidth: 75}}>
        <CardActionArea onClick={props.onClick}>
          <CardMedia
            component="img"
            width="75"
            height="60"
            image={`/api/v1/categories/${props.name}/image`}
            alt={props.name}
          />
        </CardActionArea>
      </Card>
      <Typography
        component="span"
        sx={{
          overflowWrap: 'anywhere',
          textAlign: 'center',
          lineHeight: '13px',
        }}>
        {props.name}
      </Typography>
    </Stack>
  );
};

export default Category;
