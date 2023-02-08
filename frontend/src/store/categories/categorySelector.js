export const selectCategories = state =>
  state.categories.map(category => category.name);
