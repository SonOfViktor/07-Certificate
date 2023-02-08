import {Stack, useMediaQuery} from '@mui/material';
import React from 'react';
import {useSelector} from 'react-redux';
import {selectCategories} from '../../store/categories/categorySelector';
import {useQueryParams} from '../header/hooks/useQueryParams';
import Category from './Category';

const CategoryList = React.forwardRef((_, ref) => {
  const minWidth1100 = useMediaQuery('(min-width:1100px)');
  const [changeQueryParams] = useQueryParams();
  const categoryList = useSelector(selectCategories);

  const categoryAmount = minWidth1100 ? 6 : 4;

  const handleCategory = value => {
    changeQueryParams('category', value);
  };

  return (
    <Stack
      ref={ref}
      direction="row"
      justifyContent="space-between"
      backgroundColor="var(--color-bg2)"
      p={minWidth1100 ? '20px 130px 5px 130px' : '20px 5px 10px 5px'}>
      {categoryList
        .filter((__, index) => index < categoryAmount)
        .map(category => (
          <Category
            key={category}
            name={category}
            onClick={() => handleCategory(category)}
          />
        ))}
    </Stack>
  );
});

export default CategoryList;
