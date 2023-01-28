import {Typography} from '@mui/material';
import {Stack} from '@mui/system';
import React from 'react';

const BigLogoContainer = ({children}) => (
  <Stack
    position="absolute"
    top="0"
    left="50%"
    justifyContent="center"
    width="170px"
    height="170px"
    bgcolor="#7c7979"
    borderRadius="10%"
    fontFamily="Times New Roman, Times, serif"
    sx={{
      transform: 'translate(-50%, -50%)',
      '& img': {
        position: 'absolute',
        width: '70px',
        top: '105px',
        left: '51px',
        filter:
          'drop-shadow(0px 3px 3px rgba(0, 0, 0, 0.4)) hue-rotate(180deg)',
      },
    }}>
    {children}
  </Stack>
);

const LogoText = ({children, ...props}) => (
  <Typography
    component="span"
    ml="7px"
    fontSize="55px"
    fontWeight="100"
    fontFamily="Times New Roman, Times, serif"
    lineHeight="60px"
    textTransform="uppercase"
    color="#40d47e"
    sx={{textShadow: '3px 3px 3px rgba(0, 0, 0, 0.4)'}}
    {...props}>
    {children}
  </Typography>
);

const BigLogo = () => {
  return (
    <BigLogoContainer>
      <LogoText letterSpacing="25px">CER</LogoText>
      <LogoText letterSpacing="35px">TIF</LogoText>
      <LogoText letterSpacing="94px" ml="15px">
        IE
      </LogoText>
      <img src="assets/cat-face.svg" alt="cat" />
    </BigLogoContainer>
  );
};

export default BigLogo;
